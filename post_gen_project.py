#!/usr/bin/env python3
"""
Post-template hook for Backstage Scaffolder.
Reorganizes package_path_placeholder directories to actual package structure.
"""

import os
import shutil
import sys

def move_package_path(module_name, package_name, showcase=False):
    """Move placeholder code to the correct package structure.
    
    Args:
        module_name: 'library' or 'showcase'
        package_name: The actual package name (e.g., com.example.mylib)
        showcase: If True, appends .showcase to the package
    """
    project_root = os.getcwd()
    
    if showcase:
        package_name = f"{package_name}.showcase"
    
    package_path = package_name.replace('.', os.sep)

    # We may need to move placeholder code for multiple source sets
    source_sets = ['main', 'test', 'androidTest']
    moved_any = False

    for src_set in source_sets:
        src_base = os.path.join(project_root, module_name, 'src', src_set, 'java')
        src_placeholder = os.path.join(src_base, 'package_path_placeholder')
        dst_path = os.path.join(src_base, package_path)

        if not os.path.exists(src_placeholder):
            # no placeholder for this source set — skip
            continue

        # Create destination directory structure
        if not os.path.exists(dst_path):
            os.makedirs(dst_path)

        # Move all files from placeholder to new package path
        for filename in os.listdir(src_placeholder):
            src_file = os.path.join(src_placeholder, filename)
            dst_file = os.path.join(dst_path, filename)
            
            # If it's a directory, recursively copy it
            if os.path.isdir(src_file):
                if os.path.exists(dst_file):
                    shutil.rmtree(dst_file)
                shutil.copytree(src_file, dst_file)
            else:
                shutil.copy2(src_file, dst_file)

        # Remove the placeholder directory
        shutil.rmtree(src_placeholder)
        print(f"✓ Moved {src_set} code from placeholder to {package_path} in {module_name}")
        moved_any = True

    if not moved_any:
        print(f"⚠ No placeholder directories found for module {module_name}")
        return False

    return True

if __name__ == "__main__":
    # Get package name from environment or stdin
    if len(sys.argv) > 1:
        package_name = sys.argv[1]
    else:
        package_name = input("Enter the package name (e.g., com.example.mylib): ").strip()
    
    if not package_name:
        print("Error: Package name is required")
        sys.exit(1)
    
    print(f"\n📦 Processing template with package: {package_name}\n")
    
    # Library uses base package
    move_package_path('library', package_name)
    
    # Showcase uses base package + ".showcase"
    move_package_path('showcase', package_name, showcase=True)
    
    print("\n✅ Template processing completed successfully!")

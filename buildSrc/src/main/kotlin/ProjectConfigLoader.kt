import java.util.Properties
import java.io.File

fun loadProjectConfig(root: File): Properties {
    val props = Properties()
    val cfg = File(root, "project-config.properties")
    if (cfg.exists()) cfg.inputStream().use { props.load(it) }
    return props
}

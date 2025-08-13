import net.merchantcalico.fiveguysburgersandfries.gradle.Properties
import org.gradle.jvm.tasks.Jar

plugins {
	id("maven-publish")
	alias(libs.plugins.loom)
	alias(libs.plugins.mod.publish)
}

version = "${Properties.MOD_VERSION}+${libs.versions.minecraft.get()}"
base.archivesName = Properties.MOD_ID

repositories {
	maven("https://maven.parchmentmc.org") {
		name = "ParchmentMC"
	}
}

dependencies {
	minecraft(libs.minecraft)
	mappings(loom.layered {
		officialMojangMappings()
		parchment(libs.parchment)
	})

	modImplementation(libs.fabric.loader)
	modImplementation(libs.fabric.api)
}

tasks {
	val expandProps = mapOf(
		"version" to Properties.MOD_VERSION,
		"mod_id" to Properties.MOD_ID,
		"mod_name" to Properties.MOD_NAME,
		"mod_description" to Properties.DESCRIPTION,
		"homepage" to Properties.MODRINTH_PAGE,
		"issues" to "${Properties.GITHUB_REPO}/issues",
		"sources" to Properties.GITHUB_REPO,
		"license" to Properties.LICENSE,
		"author" to Properties.MOD_AUTHOR,
		"minecraft_range" to Properties.MINECRAFT_RANGE,
		"fabric_loader_range" to Properties.FABRIC_LOADER_RANGE
	)

	val processResourcesTasks = listOf("processResources", "processTestmodResources", "processGametestResources", "processDatagenResources")

	withType<ProcessResources>().matching { processResourcesTasks.contains(it.name) }.configureEach {
		inputs.properties(expandProps)
		filesMatching("fabric.mod.json") {
			expand(expandProps)
		}
	}

	withType<JavaCompile>().configureEach {
		options.encoding = "UTF-8"
		options.release = 21
	}
}

java {
	withSourcesJar()
	sourceCompatibility = JavaVersion.toVersion(Properties.JAVA_VERSION)
	targetCompatibility = JavaVersion.toVersion(Properties.JAVA_VERSION)
}


publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifactId = base.archivesName.get()
			from(components["java"])
		}
	}
	repositories {
	}
}

publishMods {
	file.set(tasks.named<Jar>("remapJar").get().archiveFile)
	modLoaders.add("fabric")
	changelog = providers.environmentVariable("CHANGELOG")
	displayName = "v${Properties.MOD_VERSION} (Fabric ${libs.versions.minecraft.get()})"
	version = "${Properties.MOD_VERSION}+${libs.versions.minecraft.get()}-fabric"
	type = STABLE

	modrinth {
		projectId = Properties.MODRINTH_PROJECT_ID
		accessToken = providers.environmentVariable("MODRINTH_TOKEN")

		minecraftVersions.addAll(Properties.SUPPORTED_MINECRAFT_VERSIONS)
	}
}

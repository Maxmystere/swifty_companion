package tech.maxdevcoveries.swiftycompanion.lib

import com.google.gson.annotations.SerializedName

data class FortyTwoImageVersion(
    @SerializedName("large")
    val large:String,
    @SerializedName("medium")
    val medium:String,
    @SerializedName("small")
    val small:String,
    @SerializedName("micro")
    val micro:String,
)

data class FortyTwoProfileImage(
    @SerializedName("link")
    val link:String,
    @SerializedName("versions")
    val versions:FortyTwoImageVersion,
)

data class CursusUser(
    @SerializedName("cursus_id")
    val cursusId:Int,
    @SerializedName("level")
    val level:Float,
)

data class ProjectInfo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
)

data class UserProject(
    @SerializedName("id")
    val id: Int,
    @SerializedName("final_mark")
    val finalMark: Int?,
    @SerializedName("status")
    val status: String,
    @SerializedName("validated?")
    val isValidated: Boolean?,
    @SerializedName("project")
    val project: ProjectInfo,
    @SerializedName("cursus_ids")
    val cursusIds: Array<Int>,
)

data class FortyTwoUser(
    @SerializedName("id")
    val id:String,
    @SerializedName("email")
    val email:String,
    @SerializedName("displayname")
    val displayName:String,
    @SerializedName("image")
    val image:FortyTwoProfileImage,
    @SerializedName("pool_month")
    val poolMonth:String,
    @SerializedName("pool_year")
    val poolYear:String,
    @SerializedName("cursus_users")
    val cursusUsers:Array<CursusUser>,
    @SerializedName("projects_users")
    val projectsUsers: Array<UserProject>,
)
{
    companion object {
        // Current main cursus is of id 21
        const val MAIN_CURSUS_ID = 21
    }

    fun getActiveCursus(): CursusUser
    {
        if (cursusUsers.isEmpty()) {
            return CursusUser(0, 0.0F)
        }
        val mainCursus = cursusUsers.find {
            it.cursusId == MAIN_CURSUS_ID
        }
        if (mainCursus != null) {
            return mainCursus
        }
        // Fallback to first found cursus if main one isn't present
        return cursusUsers.first()
    }

    fun getCursusProjects(): List<UserProject> {
        return projectsUsers.filter {
            it.cursusIds.contains(MAIN_CURSUS_ID)
        }
    }
}
package github.be.dto

data class GitHubTagDto(
    val name: String,
    val zipball_url: String,
    val tarball_url: String,
    val commit: GitHubTagCommitDto,
    val node_id: String,
)
package com.snaggly.ksw_toolkit.util.github

data class AssetMetaData(
    val url: String,
    val id: Int,
    val node_id: String,
    val name: String,
    val label: String,
    val uploader: AuthorMetaData,
    val content_type: String,
    val state: String,
    val size: Int,
    val download_count: Int,
    val created_at: String,
    val updated_at: String,
    val browser_download_url: String
)
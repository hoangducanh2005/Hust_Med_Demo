package com.example.hust_med_demo

import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionError
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

private const val TAG = "HUST_VOICE"

@OptIn(UnstableApi::class)
class AutomotiveLibraryCallback : MediaLibraryService.MediaLibrarySession.Callback {

    private val pkg = "com.example.hust_med_demo"

    private val uri1 = RawResourceDataSource.buildRawResourceUri(R.raw.kiep_ve_sau)
    private val uri2 = RawResourceDataSource.buildRawResourceUri(R.raw.hoa_hong)
    private val uri3 = RawResourceDataSource.buildRawResourceUri(R.raw.con_mua_tinh_yeu)
    private val uri4 = RawResourceDataSource.buildRawResourceUri(R.raw.mot_minh_mot_som_ban_mai)
    private val uri5 = RawResourceDataSource.buildRawResourceUri(R.raw.thang_tu_la_loi_noi_doi_cua_em)
    private val uri6 = RawResourceDataSource.buildRawResourceUri(R.raw.baby)
    private val uri7 = RawResourceDataSource.buildRawResourceUri(R.raw.havana)
    private val uri8 = RawResourceDataSource.buildRawResourceUri(R.raw.haruharu)
    private val uri9 = RawResourceDataSource.buildRawResourceUri(R.raw.shapeofyou)
    private val uri10 = RawResourceDataSource.buildRawResourceUri(R.raw.loveyourself)
    private val uri11 = RawResourceDataSource.buildRawResourceUri(R.raw.aslongasyouloveme)

    private val art1 = Uri.parse("android.resource://$pkg/drawable/art_kiep_ve_sau")
    private val art2 = Uri.parse("android.resource://$pkg/drawable/art_hoa_hong")
    private val art3 = Uri.parse("android.resource://$pkg/drawable/art_con_mua_tinh_yeu")
    private val art4 = Uri.parse("android.resource://$pkg/drawable/art_mot_minh_mot_som_ban_mai")
    private val art5 = Uri.parse("android.resource://$pkg/drawable/art_thang_tu_la_loi_noi_doi_cua_em")
    private val art6 = Uri.parse("android.resource://$pkg/drawable/baby")
    private val art7 = Uri.parse("android.resource://$pkg/drawable/havana")
    private val art8 = Uri.parse("android.resource://$pkg/drawable/haruharu")
    private val art9 = Uri.parse("android.resource://$pkg/drawable/shapeofyou")
    private val art10 = Uri.parse("android.resource://$pkg/drawable/loveyourself")
    private val art11 = Uri.parse("android.resource://$pkg/drawable/aslongasyouloveme")

    private fun createMediaItem(
        id: String,
        title: String,
        artist: String,
        mediaUri: Uri,
        artworkUri: Uri
    ): MediaItem {
        return MediaItem.Builder()
            .setMediaId(id)
            .setUri(mediaUri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title)
                    .setArtist(artist)
                    .setArtworkUri(artworkUri)
                    .setIsBrowsable(false)
                    .setIsPlayable(true)
                    .build()
            )
            .build()
    }

    private val items = listOf(
        createMediaItem("track_1", "Kiếp Ve Sầu", "Đan Trường", uri1, art1),
        createMediaItem("track_2", "Hoa Hồng", "Hà Anh Tuấn", uri2, art2),
        createMediaItem("track_3", "Cơn Mưa Tình Yêu", "Hà Anh Tuấn", uri3, art3),
        createMediaItem("track_4", "Một mình một sớm ban mai", "Hà Anh Tuấn", uri4, art4),
        createMediaItem("track_5", "Tháng tư là lời nói dối của em", "Hà Anh Tuấn", uri5, art5),
        createMediaItem("track_6", "Baby", "Justin Bieber", uri6, art6),
        createMediaItem("track_7", "Havana", "Camila Cabello", uri7, art7),
        createMediaItem("track_8", "Haru Haru", "BIGBANG", uri8, art8),
        createMediaItem("track_9", "Shape of You", "Ed Sheeran", uri9, art9),
        createMediaItem("track_10", "Love Yourself", "Justin Bieber", uri10, art10),
        createMediaItem("track_11", "As Long As You Love Me", "Justin Bieber", uri11, art11)
    )

    private fun normalizeQuery(query: String?): String {
        return query.orEmpty()
            .lowercase()
            .replace("hust_med_demo", "")
            .replace("hust med demo", "")
            .replace("hust media", "")
            .replace("play", "")
            .replace("listen to", "")
            .replace("song", "")
            .replace("music", "")
            .replace("on", "")
            .trim()
    }

    private fun findSongs(query: String?): List<MediaItem> {
        val normalized = normalizeQuery(query)
        Log.d(TAG, "findSongs(): raw=[$query], normalized=[$normalized]")

        if (normalized.isBlank()) return emptyList()

        val titleMatches = items.filter {
            it.mediaMetadata.title?.toString()?.lowercase()?.contains(normalized) == true
        }
        if (titleMatches.isNotEmpty()) return titleMatches

        return items.filter {
            it.mediaMetadata.artist?.toString()?.lowercase()?.contains(normalized) == true
        }
    }

    private fun defaultItem(): MediaItem {
        return items.first()
    }

    private fun resolveVoiceRequest(item: MediaItem): MediaItem {
        val query = item.requestMetadata.searchQuery?.toString()
        val mediaId = item.mediaId

        Log.d(TAG, "resolveVoiceRequest(): mediaId=[$mediaId], searchQuery=[$query]")

        if (!query.isNullOrBlank()) {
            val matches = findSongs(query)
            if (matches.isNotEmpty()) {
                Log.d(TAG, "Matched voice query [$query] -> ${matches[0].mediaMetadata.title}")
                return matches[0]
            }

            Log.d(TAG, "No match for query [$query], fallback to default item")
            return defaultItem()
        }

        val byId = items.find { it.mediaId == mediaId }
        if (byId != null) {
            Log.d(TAG, "Matched by mediaId [$mediaId] -> ${byId.mediaMetadata.title}")
            return byId
        }

        if (item.localConfiguration?.uri != null) {
            Log.d(TAG, "Item already has playable URI")
            return item
        }

        Log.d(TAG, "No query/mediaId/URI, fallback to default item")
        return defaultItem()
    }

    override fun onAddMediaItems(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        Log.d(TAG, "onAddMediaItems() from ${controller.packageName}, size=${mediaItems.size}")

        val resolved = mediaItems.map { resolveVoiceRequest(it) }.toMutableList()

        resolved.forEachIndexed { index, item ->
            Log.d(
                TAG,
                "resolved[$index]: id=${item.mediaId}, title=${item.mediaMetadata.title}, uri=${item.localConfiguration?.uri}"
            )
        }

        return Futures.immediateFuture(resolved)
    }

    override fun onSetMediaItems(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>,
        startIndex: Int,
        startPositionMs: Long
    ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
        Log.d(
            TAG,
            "onSetMediaItems() from ${controller.packageName}, size=${mediaItems.size}, startIndex=$startIndex"
        )

        val resolved = mediaItems.map { resolveVoiceRequest(it) }.toMutableList()

        return Futures.immediateFuture(
            MediaSession.MediaItemsWithStartPosition(
                resolved,
                if (startIndex >= 0) startIndex else 0,
                startPositionMs
            )
        )
    }

    override fun onGetLibraryRoot(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        Log.d(TAG, "onGetLibraryRoot() from ${browser.packageName}")

        val root = MediaItem.Builder()
            .setMediaId("root")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("Hust Media")
                    .setIsBrowsable(true)
                    .setIsPlayable(false)
                    .build()
            )
            .build()

        return Futures.immediateFuture(LibraryResult.ofItem(root, params))
    }

    override fun onGetChildren(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        Log.d(TAG, "onGetChildren() from ${browser.packageName}, parentId=$parentId")

        val children = if (parentId == "root") items else emptyList()
        return Futures.immediateFuture(LibraryResult.ofItemList(ImmutableList.copyOf(children), params))
    }

    override fun onSearch(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        val matches = findSongs(query)
        Log.d(TAG, "onSearch() from ${browser.packageName}, query=[$query], count=${matches.size}")
        session.notifySearchResultChanged(browser, query, matches.size, params)
        return Futures.immediateFuture(LibraryResult.ofVoid())
    }

    override fun onGetSearchResult(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        val matches = findSongs(query)
        Log.d(TAG, "onGetSearchResult() from ${browser.packageName}, query=[$query], count=${matches.size}")
        return Futures.immediateFuture(LibraryResult.ofItemList(ImmutableList.copyOf(matches), params))
    }

    override fun onGetItem(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        mediaId: String
    ): ListenableFuture<LibraryResult<MediaItem>> {
        Log.d(TAG, "onGetItem() from ${browser.packageName}, mediaId=$mediaId")

        val item = items.find { it.mediaId == mediaId }
        return if (item != null) {
            Futures.immediateFuture(LibraryResult.ofItem(item, null))
        } else {
            Futures.immediateFuture(
                LibraryResult.ofError(SessionError(SessionError.ERROR_BAD_VALUE, "Media not found"))
            )
        }
    }

    override fun onSubscribe(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        Log.d(TAG, "onSubscribe() from ${browser.packageName}, parentId=$parentId")
        return Futures.immediateFuture(LibraryResult.ofVoid())
    }
}

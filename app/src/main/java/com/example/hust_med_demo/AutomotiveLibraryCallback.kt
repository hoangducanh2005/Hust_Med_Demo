package com.example.hust_med_demo

import android.net.Uri
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

@OptIn(UnstableApi::class)
class AutomotiveLibraryCallback : MediaLibraryService.MediaLibrarySession.Callback {

    private val pkg = "com.example.hust_med_demo"

    // Khởi tạo đường dẫn URI tới file nhạc trong res/raw
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

    // Khởi tạo đường dẫn URI tới ảnh bìa trong res/drawable
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

    // Map mediaId -> URI file nhạc thật
    private val tracks: Map<String, Uri> = mapOf(
        "track_1" to uri1,
        "track_2" to uri2,
        "track_3" to uri3,
        "track_4" to uri4,
        "track_5" to uri5,
        "track_6" to uri6,
        "track_7" to uri7,
        "track_8" to uri8,
        "track_9" to uri9,
        "track_10" to uri10,
        "track_11" to uri11
    )

    // Map mediaId -> URI ảnh bìa
    private val artworks: Map<String, Uri> = mapOf(
        "track_1" to art1,
        "track_2" to art2,
        "track_3" to art3,
        "track_4" to art4,
        "track_5" to art5,
        "track_6" to art6,
        "track_7" to art7,
        "track_8" to art8,
        "track_9" to art9,
        "track_10" to art10,
        "track_11" to art11
    )

    // Hàm helper để tạo MediaItem
    private fun createMediaItem(id: String, title: String, artist: String, mediaUri: Uri, artworkUri: Uri): MediaItem {
        return MediaItem.Builder()
            .setMediaId(id)
            .setUri(mediaUri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title)
                    .setArtist(artist)
                    .setArtworkUri(artworkUri) // Thiết lập ảnh bìa
                    .setIsBrowsable(false) //// Đây o là 1 folder, ko thể browse ( nó là node con )
                    .setIsPlayable(true)
                    .build()
            )
            .build()
    }

    // Danh sách hiển thị lên màn hình
    private val items = listOf( // dùng hàm createMediaItem nhanh hơn khai báo từng bài code cũ
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

    override fun onGetLibraryRoot(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        val root = MediaItem.Builder()
            .setMediaId("root")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("My Media")
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
        val children = when (parentId) {
            "root" -> listOf(
                MediaItem.Builder()
                    .setMediaId("songs")
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("Songs")
                            .setIsBrowsable(true)
                            .setIsPlayable(false)
                            .build()
                    ).build()
            )
            "songs" -> items
            else -> emptyList()
        }
        return Futures.immediateFuture(LibraryResult.ofItemList(ImmutableList.copyOf(children), params))
    }

    override fun onAddMediaItems(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val resolved = mediaItems.map { item ->
            val uri = tracks[item.mediaId] //Map mediaID -> uri - Item hiển thị -> item để phát
            val art = artworks[item.mediaId]
            if (uri != null) {
                item.buildUpon()
                    .setUri(uri)
                    .setMediaMetadata(
                        item.mediaMetadata.buildUpon()
                            .setArtworkUri(art) // Gắn ảnh bìa khi phát
                            .setIsPlayable(true)
                            .setIsBrowsable(false)
                            .build()
                    )
                    .build()
            } else {
                item
            }
        }.toMutableList()
        return Futures.immediateFuture(resolved)
    }

    override fun onGetItem(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        mediaId: String
    ): ListenableFuture<LibraryResult<MediaItem>> {
        val item = items.firstOrNull { it.mediaId == mediaId }
        return Futures.immediateFuture(
            if (item != null) {
                LibraryResult.ofItem(item, null)
            } else {
                LibraryResult.ofError(SessionError(SessionError.ERROR_BAD_VALUE, "Unknown mediaId: $mediaId"))
            }
        )
    }

    override fun onSubscribe(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        return Futures.immediateFuture(LibraryResult.ofVoid(null))
    }
}

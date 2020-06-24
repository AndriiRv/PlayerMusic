function getCountOfFavouriteByMusicId(musicId) {
    let result = null;
    $.get({
        url: '/statistic/favourite',
        data: {
            musicId: musicId,
        },
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}
function getCountOfPlayedByMusicId(musicId) {
    let result = null;
    $.get({
        url: '/statistic/played',
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

function addCountOfPlayedByMusicId(musicId) {
    let result = null;
    $.post({
        url: '/statistic/played',
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
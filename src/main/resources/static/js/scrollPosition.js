$(".playInJava").on("click", function () {
    sessionStorage.setItem("scroll", window.pageYOffset);
});
if (sessionStorage.getItem('scroll')) {
    window.scrollTo(0, parseInt(sessionStorage.getItem("scroll")));
}
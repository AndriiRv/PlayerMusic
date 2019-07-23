$(".playInJava").on("click", function () {
    localStorage.setItem("scroll", window.pageYOffset);
});
if (localStorage.getItem('scroll')) {
    window.scrollTo(0, parseInt(localStorage.getItem("scroll")));
}
window.localStorage.clear();
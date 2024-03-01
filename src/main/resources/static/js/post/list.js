
var selectedCategory = "전체글";   // 선택한 카테고리

$(document).ready(function($) {
    // 카테고리 클릭 이벤트
    $(".categoryClass").click(function(event) {
        updateSelectedLink($(this));
    }).hover(function() {
        $(this).toggleClass("hovered");
    });

    document.getElementById('categoryName').textContent = selectedCategory;
})

// 선택한 카테고리 업데이트
function updateSelectedLink($link) {
    $(".categoryClass").removeClass("selected");
    $link.addClass("selected");
    selectedCategory = $link.text();
        document.getElementById('categoryName').textContent = selectedCategory;
}


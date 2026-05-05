// Maven Site 智能菜单折叠脚本
// 功能：当前页面所在菜单保持展开，其他菜单默认折叠

$(document).ready(function() {
    // 获取当前页面的路径
    var currentPath = window.location.pathname;
    var currentPage = currentPath.substring(currentPath.lastIndexOf('/') + 1);
    
    // 如果路径为空，使用 index.html
    if (!currentPage || currentPage === '') {
        currentPage = 'index.html';
    }
    
    console.log('当前页面:', currentPage);
    
    // 找到所有菜单标题（nav-header）
    var menuHeaders = $('.nav-list li.nav-header');
    
    // 标记是否找到当前页面对应的菜单
    var foundActiveMenu = false;
    
    // 遍历所有菜单
    menuHeaders.each(function() {
        var header = $(this);
        var submenu = header.next('ul');
        
        // 检查这个菜单下是否有当前页面的链接
        var hasCurrentPage = false;
        submenu.find('a').each(function() {
            var href = $(this).attr('href');
            if (href && href.indexOf(currentPage) !== -1) {
                hasCurrentPage = true;
                // 高亮当前页面的链接
                $(this).parent('li').addClass('active');
                return false; // 跳出循环
            }
        });
        
        if (hasCurrentPage) {
            // 如果是当前页面所在的菜单，保持展开
            submenu.show();
            header.removeClass('collapsed');
            header.addClass('expanded');
            foundActiveMenu = true;
            console.log('展开活动菜单:', header.text().trim());
        } else {
            // 其他菜单默认折叠
            submenu.hide();
            header.addClass('collapsed');
            header.removeClass('expanded');
        }
    });
    
    // 如果没有找到活动菜单（比如在首页），折叠所有菜单
    if (!foundActiveMenu) {
        console.log('未找到活动菜单，折叠所有菜单');
        menuHeaders.each(function() {
            $(this).next('ul').hide();
            $(this).addClass('collapsed');
        });
    }
    
    // 为所有菜单标题添加点击事件，实现展开/折叠切换
    menuHeaders.click(function(e) {
        e.preventDefault();
        var header = $(this);
        var submenu = header.next('ul');
        
        if (submenu.is(':visible')) {
            // 当前是展开状态，点击后折叠
            submenu.slideUp(200);
            header.addClass('collapsed');
            header.removeClass('expanded');
        } else {
            // 当前是折叠状态，点击后展开
            submenu.slideDown(200);
            header.removeClass('collapsed');
            header.addClass('expanded');
        }
    });
    
    // 添加鼠标悬停效果，提升用户体验
    menuHeaders.hover(
        function() {
            $(this).css('cursor', 'pointer');
        },
        function() {
            $(this).css('cursor', 'default');
        }
    );
    
    console.log('菜单折叠初始化完成');
});

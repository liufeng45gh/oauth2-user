<div id="header">

  <div id="topbar">
       <div class="pull-right" id="topnav">
             <div class="dropdown" id="userMenu" >
                <a href="javascript:;" data-toggle="dropdown">
                <i class="icon-user"></i> <span>${Session["KEY_CMS_USER"].nickName?default("")}， 超级管理员</span> <span class="caret"></span>
                </a>
             </div>
             <div class="dropdown"  >
                <a href="/cms/logout"><span>退出</span></a>
             </div>

             <div class="dropdown">
                <a href="javascript:;" data-toggle="dropdown">帮助 <span class="caret"></span></a>
             </div>
             <div class="dropdown">
                <a href="/misc-about.html" class="about iframe" data-width="900" data-headerless="true" data-class="modal-about">关于</a>
             </div>

        </div>
      <h5 id="companyname" style="text-align:left;">
        oauth2.0 用户管理系统
        </h5>
    </div>

	<div id="mainmenu">
	
		<ul class="nav">
		<!-- <li  id="top_munu_app"><a href="/cms/app/list" >应用</a></li> -->
		<li  id="top_menu_user"><a href="/cms/user/list?page=1" >会员</a></li>				
		<li  id="top_menu_self"><a href="/cms/self/welcome">系统</a></li>
		</ul>
	 
	</div>


</div>
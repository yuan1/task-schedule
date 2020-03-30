<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@ include file="/common/_meta.jsp" %>
    <title>计算机任务调度</title>
</head>
<body>

<header id="header">
    <h3 class="header-title">计算机任务调度</h3>
</header>
<div id="container">
    <div id="flexCon">
        <div class="flex-row">
            <div class="flex-cell flex-cell-l">
                <div class="chart-wrapper">
                    <h3 class="chart-title">
                        <label for="computer-select">
                            计算机状态：
                        </label>

                        <select id="computer-select">
                            <option value="1">计算机1</option>
                            <option value="2">计算机2</option>
                            <option value="3">计算机3</option>
                        </select>
                    </h3>
                    <div class="chart-div">
                        <div id="computer-status" style="height: calc(100% - 100px)"></div>
                        <div class="computer-info" style="height: 100px">
                            <h2>配置信息：</h2>
                            <p>CPU:2.5GHZ, 内存:2G, 硬盘:500G, 带宽:100M</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="flex-cell flex-cell-c">
                <div class="chart-wrapper">
                    <h3 class="chart-title">统计</h3>
                    <div class="chart-div">
                        <div id="task-count" style="height: 100%"></div>
                    </div>
                </div>
            </div>
            <div class="flex-cell flex-cell-r">
                <div class="chart-wrapper">
                    <h3 class="chart-title">计算机管理</h3>
                    <div class="chart-div">
                        <div class="manager-list">
                            <p style="flex: 1">名称</p>
                            <p style="width: 50px">CPU</p>
                            <p style="width: 50px">内存</p>
                            <p style="width: 50px">硬盘</p>
                            <p style="width: 50px">带宽</p>
                            <p style="width: 100px">操作</p>
                        </div>
                        <div class="computer-manager" id="computer-manager">
                            <div class="chart-loader">
                                <div class="loader"></div>
                            </div>
                            <div way-repeat="computer.list" class="manager-list">
                                <p way-data="name" style="flex: 1"></p>
                                <p way-data="cpu" style="width: 50px"></p>
                                <p way-data="memory" style="width: 50px"></p>
                                <p way-data="disk" style="width: 50px"></p>
                                <p way-data="network" style="width: 50px"></p>
                                <p style="width: 100px">
                                    <a href="#">任务</a>丨 <a href="#">删除</a>
                                </p>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <div class="flex-row">
            <div class="flex-cell flex-cell-lc">
                <div class="chart-wrapper" style="height: calc(100% - 100px)">
                    <h3 class="chart-title">计算机负载排行</h3>
                    <div class="chart-div task-top">
                        <div class="task-top-content">
                            <h2>CPU负载</h2>
                            <div class="task-top-progress">
                                <label>计算机1</label>
                                <span>80%</span>
                                <div class="progress-bar"><p></p></div>
                            </div>
                        </div>
                        <div class="task-top-content">
                            <h2>内存负载</h2>
                            <div class="task-top-progress">
                                <label>计算机1</label>
                                <span>80%</span>
                                <div class="progress-bar"><p></p></div>
                            </div>
                        </div>
                        <div class="task-top-content">
                            <h2>硬盘负载</h2>
                            <div class="task-top-progress">
                                <label>计算机1</label>
                                <span>80%</span>
                                <div class="progress-bar"><p></p></div>
                            </div>
                        </div>
                        <div class="task-top-content">
                            <h2>带宽负载</h2>
                            <div class="task-top-progress">
                                <label>计算机1</label>
                                <span>80%</span>
                                <div class="progress-bar"><p></p></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="chart-div task-log">
                    <p>2020-10-01 09:10:10 任务1 已经分配到计算机1</p>
                    <p>2020-10-01 09:10:10 任务1 已经分配到计算机1</p>
                    <p>2020-10-01 09:10:10 任务1 已经分配到计算机1</p>
                    <p>2020-10-01 09:10:10 任务1 已经分配到计算机1</p>
                    <p>2020-10-01 09:10:10 任务1 已经分配到计算机1</p>
                    <p>2020-10-01 09:10:10 任务1 已经分配到计算机1</p>
                </div>
            </div>
            <div class="flex-cell flex-cell-r">
                <div class="chart-wrapper">
                    <h3 class="chart-title">任务管理</h3>
                    <div class="chart-div">
                        <div class="manager-list">
                            <p style="flex: 1">名称</p>
                            <p style="width: 100px">状态</p>
                            <p style="width: 50px">操作</p>
                        </div>
                        <div class="task-manager">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/common/_footer.jsp" %>
</body>
</html>
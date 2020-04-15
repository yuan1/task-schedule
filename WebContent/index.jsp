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
                    <div class="chart-title" id="computer-select">
                        <label>
                            计算机状态：<span way-data="computer.show.name"></span>
                        </label>
                        <div class="computer-select" id="computer-select-content">
                            <div way-repeat="computer.list" class="computer-select-item" onclick="showComputer($$key)">
                                <span way-data="name"></span>
                            </div>
                        </div>

                    </div>
                    <div class="chart-div" id="computer-show">
                        <div class="chart-loader">
                            <div class="loader"></div>
                        </div>
                        <div id="computer-status" style="height: calc(100% - 100px)"></div>
                        <div class="computer-info" style="height: 100px">
                            <h2>配置信息：</h2>
                            <p>CPU: <span way-data="computer.show.cpu"></span>, 内存: <span
                                    way-data="computer.show.memory"></span>, 硬盘: <span
                                    way-data="computer.show.disk"></span>, 带宽: <span
                                    way-data="computer.show.network"></span></p>
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
                    <h3 class="chart-title">计算机管理 <a href="javascript:;" onclick="showAddComputerDialog()">+</a></h3>
                    <div class="chart-div">
                        <div class="manager-list">
                            <p style="flex: 1">名称</p>
                            <p style="width: 50px">CPU</p>
                            <p style="width: 50px">内存</p>
                            <p style="width: 50px">硬盘</p>
                            <p style="width: 50px">带宽</p>
                            <p style="width: 50px">操作</p>
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
                                <p style="width: 50px">
                                    <a href="javascript:;" onclick="doDeleteComputer($$key)">删除</a>
                                </p>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <div class="flex-row">
            <div class="flex-cell flex-cell-lc">
                <div class="chart-wrapper">
                    <h3 class="chart-title">计算机负载排行</h3>
                    <div class="chart-div task-top">
                        <div class="task-top-content">
                            <h2>CPU负载</h2>
                            <div class="task-top-progress" way-repeat="computer.cpuTopList">
                                <label way-data="name">计算机1</label>
                                <span way-data="cpuUsage">80</span>
                                <div class="progress-bar"><p way-data="cpuUsage" id="task-cpu-top-$$key"></p></div>
                            </div>
                        </div>
                        <div class="task-top-content">
                            <h2>内存负载</h2>
                            <div class="task-top-progress" way-repeat="computer.memoryTopList">
                                <label way-data="name">计算机1</label>
                                <span way-data="memoryUsage">80</span>
                                <div class="progress-bar"><p way-data="memoryUsage" id="task-memory-top-$$key"></p>
                                </div>
                            </div>
                        </div>
                        <div class="task-top-content">
                            <h2>硬盘负载</h2>
                            <div class="task-top-progress" way-repeat="computer.diskTopList">
                                <label way-data="name">计算机1</label>
                                <span way-data="diskUsage">80%</span>
                                <div class="progress-bar"><p way-data="diskUsage" id="task-disk-top-$$key"></p></div>
                            </div>
                        </div>
                        <div class="task-top-content">
                            <h2>带宽负载</h2>
                            <div class="task-top-progress" way-repeat="computer.networkTopList">
                                <label way-data="name">计算机1</label>
                                <span way-data="networkUsage">80%</span>
                                <div class="progress-bar"><p way-data="networkUsage" id="task-network-top-$$key"></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="flex-cell flex-cell-r">
                <div class="chart-wrapper">
                    <h3 class="chart-title">任务管理 <a href="javascript:;" onclick="showAddTaskDialog()">+</a>
                        <span style="font-size: 12px;position: absolute;right: 10px;top: 2px;display: block"><a href="javascript:;" onclick="setTaskCanStart(true)">开始任务执行</a> 丨<a href="javascript:;"  onclick="setTaskCanStart(false)">停止任务执行</a></span>
                    </h3>
                    <div class="chart-div">
                        <div class="manager-list">
                            <p style="flex: 1">名称</p>
                            <p style="width: 50px">CPU</p>
                            <p style="width: 50px">内存</p>
                            <p style="width: 50px">硬盘</p>
                            <p style="width: 50px">带宽</p>
                            <p style="width: 100px">计算机</p>
                            <p style="width: 100px">状态</p>
                            <p style="width: 50px">操作</p>
                        </div>
                        <div class="task-manager" id="task-manager">
                            <div class="chart-loader">
                                <div class="loader"></div>
                            </div>
                            <div way-repeat="task.list" class="manager-list">
                                <p way-data="name" style="flex: 1"></p>
                                <p way-data="cpuUsage" style="width: 50px"></p>
                                <p way-data="memoryUsage" style="width: 50px"></p>
                                <p way-data="diskUsage" style="width: 50px"></p>
                                <p way-data="networkUsage" style="width: 50px"></p>
                                <p way-data="computerName" style="width: 100px"></p>
                                <p way-data="status" style="width: 100px"></p>
                                <p style="width: 50px">
                                    <a href="javascript:;" onclick="doDeleteTask($$key)">删除</a>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="computer-dialog" class="dialog chart-done">
    <div class="dialog-body">
        <h2>计算机添加</h2>
        <div class="dialog-content">
            <div class="chart-loader">
                <div class="loader"></div>
            </div>
            <form way-data="computer.form">
                <div class="form-item is-required">
                    <label for="computer_name">名称：</label>
                    <input type="text" id="computer_name" name="name">
                </div>
                <div class="form-item is-required">
                    <label for="computer_cpu">CPU：</label>
                    <input type="text" id="computer_cpu" name="cpu">
                </div>
                <div class="form-item is-required">
                    <label for="computer_memory">内存大小：</label>
                    <input type="text" id="computer_memory" name="memory">
                </div>
                <div class="form-item is-required">
                    <label for="computer_disk">硬盘容量：</label>
                    <input type="text" id="computer_disk" name="disk">
                </div>
                <div class="form-item is-required">
                    <label for="computer_network">网络带宽：</label>
                    <input type="text" id="computer_network" name="network">
                </div>
            </form>
        </div>
        <div class="dialog-footer">
            <button onclick="doAddComputer()">确定</button>
            <button onclick="closeDialog()">取消</button>
        </div>
    </div>
</div>
<div id="task-dialog" class="dialog chart-done">
    <div class="dialog-body">
        <h2>任务添加</h2>
        <div class="dialog-content">
            <div class="chart-loader">
                <div class="loader"></div>
            </div>
            <form way-data="task.form">
                <div class="form-item is-required">
                    <label for="task_name">名称：</label>
                    <input type="text" id="task_name" name="name">
                </div>
                <div class="form-item is-required">
                    <label for="task_cpu">所需CPU(%)：</label>
                    <input type="text" id="task_cpu" name="cpuUsage">
                </div>
                <div class="form-item is-required">
                    <label for="task_memory">所需内存(%)：</label>
                    <input type="text" id="task_memory" name="memoryUsage">
                </div>
                <div class="form-item is-required">
                    <label for="task_disk">所需硬盘(%)：</label>
                    <input type="text" id="task_disk" name="diskUsage">
                </div>
                <div class="form-item is-required">
                    <label for="task_network">所需网络(%)：</label>
                    <input type="text" id="task_network" name="networkUsage">
                </div>
                <div class="form-item is-required">
                    <label for="task_time">时长(秒)：</label>
                    <input type="text" id="task_time" name="timeUsage">
                </div>
                <div class="form-item is-required">
                    <label for="task_count">任务数量：</label>
                    <input type="text" id="task_count" name="count">
                </div>
                <div class="form-item is-required">
                    <label for="task_count_interval">任务间隔(秒)：</label>
                    <input type="text" id="task_count_interval" name="countInterval">
                </div>
            </form>
        </div>
        <div class="dialog-footer">
            <button onclick="doAddTask()">确定</button>
            <button onclick="closeDialog()">取消</button>
        </div>
    </div>
</div>
<%@ include file="/common/_footer.jsp" %>
</body>
</html>
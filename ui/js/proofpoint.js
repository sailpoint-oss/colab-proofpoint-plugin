/**
 * Create the module.
 */
var proofpointModule = angular.module('Proofpoint', ['ui.bootstrap', 'angularUtils.directives.dirPagination']);

proofpointModule.config(function ($httpProvider) {
    $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
});

proofpointModule.constant('constants', {
    settings: '/settings',
    refreshAppList: '/refreshAppList',
    refreshVAPList: '/refreshVAPList',
    pluginName: 'ProofpointPlugin'
});

/**
 * Controller for the application and user list.
 */
proofpointModule.controller('proofpointController', function (settingsService, vapListService, $q, $uibModal) {

    var proofpointController = this;

    proofpointController.currentPage = 1;

    proofpointController.lastUpdated = " ";

    proofpointController.vapListAge = "";

    proofpointController.appList = [];

    proofpointController.vapList = [];

    proofpointController.spinRefreshIcon = false;

    proofpointController.disableSave = false;

    proofpointController.hasAppData = false;

    proofpointController.hasVapData = false;

    proofpointController.notification = false;

    proofpointController.errors = {
        applnError: false
    };

    proofpointController.success = {
        applnSuccess: false
    };

    proofpointController.clear = function () {
        angular.forEach(this.appList, function (application) {
            application.selected = false;
        });

        angular.forEach(this.vapList, function (user) {
            user.selected = false;
        });

        proofpointController.disableSave = false;
    };

    proofpointController.formatDate = function () {
        if (this.lastUpdated) {
            let date = new Date(this.lastUpdated);
            const newDateFormat = date.toLocaleDateString();
            const newTimeFormat = date.toLocaleTimeString();
            return newDateFormat + " " + newTimeFormat;
        } else {
            return "";
        }
    };

    proofpointController.resetCommit = function () {
        proofpointController.errors.applnError = false;
        proofpointController.success.applnSuccess = false;
        proofpointController.disableSave = true;
    };

    proofpointController.refreshAppList = function () {
        proofpointController.spinRefreshIcon = !proofpointController.spinRefreshIcon;
        settingsService.refreshAppList().then(function (data) {
            if (data.appList != null && data.appList.length > 0) {
                proofpointController.hasAppData = true;
            }

            proofpointController.appList = data.appList;
        });
    };

    proofpointController.refreshVapList = function () {
        proofpointController.spinRefreshIcon = !proofpointController.spinRefreshIcon;
        vapListService.refreshVapList().then(function (data) {
            if (data.vapList != null && data.vapList.length > 0) {
                proofpointController.hasVapData = true;
            }

            proofpointController.vapList = data.vapList;
            proofpointController.lastUpdated = data.lastUpdated;
            proofpointController.vapListAge = data.vapListAge;
        });

    };

    proofpointController.setSaveButton = function () {
        proofpointController.disableSave = false;
    };

    proofpointController.setState = function () {
        proofpointController.notification = false;
        proofpointController.errors.applnError = false;
        proofpointController.success.applnSuccess = false;

        return false;
    };

    proofpointController.saveSettings = function () {
        settingsService.saveSettings(this.appList, this.vapList).then(function (data) {
            proofpointController.success.applnSuccess = true;
            proofpointController.errors.applnError = false;
            proofpointController.disableSave = true;
        }).catch(function (response) {
            proofpointController.success.applnSuccess = false;
            proofpointController.errors.applnError = true;
            proofpointController.disableSave = false;
        });
    };

    settingsService.getSettings()
        .then(function (data) {
            if (data != null) {
                if (data.vapList != null && data.vapList.length > 0) {
                    proofpointController.hasVapData = true;
                }

                proofpointController.appList = data.appList;
                proofpointController.vapList = data.vapList;
                proofpointController.lastUpdated = data.lastUpdated;
                proofpointController.vapListAge = data.vapListAge;
            }
        });

    settingsService.refreshAppList()
        .then(function (data) {
            if (data != null && data.appList != null && data.appList.length > 0) {
                proofpointController.hasAppData = true;
            }

            proofpointController.appList = data.appList;
        });
});

proofpointModule.factory('settingsService', function (constants, $http) {

    return {
        getSettings: function () {
            var url = PluginHelper.getPluginRestUrl(constants.pluginName + constants.settings);
            return $http.get(url)
                .then(function (response) {
                    return response.data;
                });
        },

        saveSettings: function (appList, vapList) {
            var url = PluginHelper.getPluginRestUrl(constants.pluginName + constants.settings);
            var body = {
                appList: appList,
                vapList: vapList
            };
            return $http.put(url, body)
                .then(function (response) {
                    return response.data;
                });
        },

        refreshAppList: function () {
            var url = PluginHelper.getPluginRestUrl(constants.pluginName + constants.refreshAppList);
            return $http.get(url)
                .then(function (response) {
                    return response.data;
                });
        }
    }
});

proofpointModule.factory('vapListService', function (constants, $http) {
    return {
        refreshVapList: function () {
            var url = PluginHelper.getPluginRestUrl(constants.pluginName + constants.refreshVAPList);
            return $http.get(url)
                .then(function (response) {
                    return response.data;
                });
        }
    }
});

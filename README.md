Получаем данные истории браузера:
adb shell content query --uri content://org.mozilla.firefox.db.browser/history
Изменяем историю. Например:
adb shell content insert --uri content://org.mozilla.firefox.db.browser/history --bind title:s:tut.by --bind url:s:https://www.tut.by --bind visits:i:88 --bind date:i:1552015774780



CircleCi:
[![CircleCI](https://circleci.com/gh/theManWithoutQualities/konsttest2.svg?style=svg)](https://circleci.com/gh/theManWithoutQualities/konsttest2)
AppCenter:
[![Build status](https://build.appcenter.ms/v0.1/apps/cce460f7-bf0e-4628-8a59-1e9583b78f81/branches/master/badge)](https://appcenter.ms)
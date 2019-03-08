Получаем данные истории браузера:
adb shell content query --uri content://org.mozilla.firefox.db.browser/history
Изменяем историю. Например:
adb shell content insert --uri content://org.mozilla.firefox.db.browser/history --bind title:s:tut.by --bind url:s:https://www.tut.by --bind visits:i:88 --bind date:i:1552015774780
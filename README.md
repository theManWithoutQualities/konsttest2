Изменение данных взломанного contentProvider:
adb shell content update --uri content://startCount/start_count --bind count:i:99999
В результате при очередном создании MainActivity  в лог выводится данное значение количества созданий.
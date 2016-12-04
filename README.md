<b>Приложение для получения новостей с сайта F1news.ru</b>

#Скриншоты приложения

![navdrawer](https://cloud.githubusercontent.com/assets/12079742/20848933/5ac28c2a-b8e5-11e6-81e4-9449fda3019e.png)
![cards_list](https://cloud.githubusercontent.com/assets/12079742/20848927/52e1d6d2-b8e5-11e6-8a4b-6d5d016b39ff.png)
![list](https://cloud.githubusercontent.com/assets/12079742/20848930/57175f56-b8e5-11e6-8891-42387ed4a985.png)
![page](https://cloud.githubusercontent.com/assets/12079742/20869292/48fef39c-ba80-11e6-8530-a1deb70cbb3d.png)
![slide_page](https://cloud.githubusercontent.com/assets/12079742/20869291/48f6a246-ba80-11e6-891f-fb2bacfb2c9d.png)
![table](https://cloud.githubusercontent.com/assets/12079742/20869299/693ae7ce-ba80-11e6-98f9-30694e8f90ab.png)
![interview](https://cloud.githubusercontent.com/assets/12079742/20869300/69440958-ba80-11e6-8109-2f67c3834fb5.png)
![settings_all](https://cloud.githubusercontent.com/assets/12079742/20848945/6504b01e-b8e5-11e6-8cc6-b0e6e8c9c9ca.png)
![settings_autorefresh](https://cloud.githubusercontent.com/assets/12079742/20848950/67c6738c-b8e5-11e6-8d92-baed2e2bc454.png)
![settings_list](https://cloud.githubusercontent.com/assets/12079742/20848953/6a92a324-b8e5-11e6-9ace-d580047b12fb.png)

***
#Описание

Приложение парсит RSS сайта F1news.ru, выбирает ссылки на новости, разбивает ссылки по разделам, по ссылкам выгружаются новости со страниц в базу SQLite. Контроль уникальности новости (отсутствия ее в базе) осуществляется по ссылке на новость - хранится в базе. 

Выборка ссылок на новости осуществляется в отдельном потоке, далее стартует многопоточная загрузка новостей (для каждой новости отдельный поток - параллельная загрузка, временно отключено). 
Текст новости форматируется аналогично сайту - пока выполнено форматирование текста и таблиц.
Изображения к новости хранятся в папке с приложением (при переносе приложения на карту памяти, перенесутся вместе с приложением). 

В настройках возможно включить вывод только непрочитанных новостей (после просмотра новость больше не выводится в списке - если настройку отключить, выводятся все новости). Вывод новостей обычным списком или списком карточек выбирается в настройках.

Имеется возможность включить обновление по расписанию с выбором интервала обновления. Реализовано через IntentService.

Релизы доступны: https://github.com/orloffski/F1News.ru-reader/releases

<b>Приложение для получения новостей с сайта F1news.ru</b>

#Скриншоты приложения

![menu](https://cloud.githubusercontent.com/assets/12079742/20441352/27f71628-add5-11e6-8d70-018fbb10b310.png)
![list](https://cloud.githubusercontent.com/assets/12079742/20441048/cb00249c-add3-11e6-91b6-77e331cb0450.png)
![page](https://cloud.githubusercontent.com/assets/12079742/20441064/dc9953b8-add3-11e6-9a34-dad4546ee4db.png)
![slide](https://cloud.githubusercontent.com/assets/12079742/20441607/45d8249c-add6-11e6-8792-bf0720320651.png)
![settings1](https://cloud.githubusercontent.com/assets/12079742/20441075/e6d610f0-add3-11e6-84b8-0546f70b83cd.png)
![settings2](https://cloud.githubusercontent.com/assets/12079742/20441080/efd3399e-add3-11e6-92aa-9e5140e94dcc.png)

***
#Описание

Приложение парсит RSS сайта F1news.ru выбирая ссылки, разбивает ссылки по разделам, по ссылкам выгружаются новости со страниц в базу SQLite. Контроль уникальности новости (отсутствия ее в базе) осуществляется по ссылке на новость - хранится в базе, также имеется проверка на соответствие ссылки разделу - т.к. встречаются ссылки не в своих разделах. 

Выборка ссылок на новости осуществляется в отдельном потоке, далее стартует многопоточная загрузка новостей (для каждой новости отдельный поток - параллельная загрузка). 

Изображения к новости хранятся в папке с приложением (если переместить на карту памяти, перенесутся вместе с приложением). 

В настройках возможно включить вывод только непрочитанных новостей (после просмотра новость больше не выводится в списке - если настройку отключить, выводятся все новости).

Имеется возможность включить обновление по расписанию с выбором интервала обновления. Реализовано через IntentService.

Релиз доступен: https://github.com/orloffski/F1News.ru-reader/blob/master/apk_RC/F1NewsReader.RC.1.030.apk.test (необходимо удалить ".test" в конце имени файла)
***
#Планируемые доработки

Реализовать CustomView для отображения текста новости (с выводом форматирования аналогичному на сайте)

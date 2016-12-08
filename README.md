<b>Приложение для получения новостей с сайта F1news.ru</b>

#Скриншоты приложения

![001_menu](https://cloud.githubusercontent.com/assets/12079742/20998812/9ecd4a1e-bd21-11e6-9d5b-46972bd5de88.png)
![002_card_list](https://cloud.githubusercontent.com/assets/12079742/20998813/9ed06f3c-bd21-11e6-99bd-3d23c20f18e0.png)
![003_list](https://cloud.githubusercontent.com/assets/12079742/20998814/9ed34e96-bd21-11e6-9395-12094227049e.png)
![004_page_top](https://cloud.githubusercontent.com/assets/12079742/20998815/9ed8fbd4-bd21-11e6-865c-a93707e713b2.png)
![005_page_table](https://cloud.githubusercontent.com/assets/12079742/20998816/9edaf574-bd21-11e6-980c-4f53f2f9f891.png)
![006_page_body_images](https://cloud.githubusercontent.com/assets/12079742/20998817/9ede9ab2-bd21-11e6-852e-85e5d1599504.png)
![007_page_slide](https://cloud.githubusercontent.com/assets/12079742/20998818/9ee96294-bd21-11e6-873f-3fd23ec4ce49.png)
![008_page_text](https://cloud.githubusercontent.com/assets/12079742/20998819/9eef74e0-bd21-11e6-85b3-d500830c5907.png)
![009_full_settings](https://cloud.githubusercontent.com/assets/12079742/20998820/9ef199be-bd21-11e6-9b1e-3de95fb52fb2.png)
![010_list_view_settings](https://cloud.githubusercontent.com/assets/12079742/20998821/9ef4d282-bd21-11e6-8283-448ac8081861.png)
![011_autorefresh_settings](https://cloud.githubusercontent.com/assets/12079742/20998822/9efa7642-bd21-11e6-82cd-3a57c3e7dd85.png)
![012_notifications](https://cloud.githubusercontent.com/assets/12079742/20998823/9efd5844-bd21-11e6-8604-2a7571568fe7.png)

***
#Описание

Приложение парсит RSS сайта F1news.ru, выбирает ссылки на новости, разбивает ссылки по разделам, по ссылкам выгружаются новости со страниц в базу SQLite. Контроль уникальности новости (отсутствия ее в базе) осуществляется по ссылке на новость - хранится в базе. 

Выборка ссылок на новости осуществляется в отдельном потоке, далее стартует многопоточная загрузка новостей (для каждой новости отдельный поток - параллельная загрузка, временно отключено). 
Текст новости форматируется аналогично сайту (выводятся таблицы, текст и рисунки в тексте новости).
Изображения к новости хранятся в папке с приложением (при переносе приложения на карту памяти, перенесутся вместе с приложением). 

В настройках возможно включить вывод только непрочитанных новостей (после просмотра новость больше не выводится в списке - если настройку отключить, выводятся все новости). Вывод новостей обычным списком или списком карточек выбирается в настройках.

Имеется возможность включить обновление по расписанию с выбором интервала обновления. Реализовано через IntentService.

Релизы доступны: https://github.com/orloffski/F1News.ru-reader/releases

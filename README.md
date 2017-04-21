#Приложение для получения новостей с сайта F1news.ru

<b>Скриншоты приложения</b>

![screenshot_2017-04-17-00-55-48-585](https://cloud.githubusercontent.com/assets/12079742/25074651/4f4178e6-2309-11e7-934d-b087a8d733aa.jpeg)
![screenshot_2017-04-17-00-56-00-098](https://cloud.githubusercontent.com/assets/12079742/25074653/4f499e7c-2309-11e7-9907-d18a842ba1b8.jpeg)
![screenshot_2017-04-17-00-56-19-865](https://cloud.githubusercontent.com/assets/12079742/25074652/4f467832-2309-11e7-88c3-e8fec3febc25.jpeg)
![screenshot_2017-04-17-00-56-32-315](https://cloud.githubusercontent.com/assets/12079742/25074654/4f4e9eb8-2309-11e7-9550-eddcd2bca6e7.jpeg)
![screenshot_2017-04-17-00-56-46-766](https://cloud.githubusercontent.com/assets/12079742/25074655/4f51f40a-2309-11e7-9017-e07c493f7e6d.jpeg)
![screenshot_2017-04-17-00-57-51-904](https://cloud.githubusercontent.com/assets/12079742/25074656/4f55a6ae-2309-11e7-9143-89357474c7b1.jpeg)
![screenshot_2017-04-17-00-57-01-637](https://cloud.githubusercontent.com/assets/12079742/25074662/6b5a1d30-2309-11e7-9d22-565b27b0c7d9.jpeg)
![screenshot_2017-04-17-00-57-11-297](https://cloud.githubusercontent.com/assets/12079742/25074663/6b5de1e0-2309-11e7-89af-8e969bd7b578.jpeg)
![screenshot_2017-04-17-00-57-20-985](https://cloud.githubusercontent.com/assets/12079742/25074664/6b613eee-2309-11e7-8cfd-eae5bc937d90.jpeg)

***
<b>Описание</b>

Приложение для чтения новостей с портала f1news.ru. Получает ссылки на новости из RSS и далее по ссылкам парсит страницы новостей (текст новости хранится в html-коде для дальнейшего культурного вывода, пока только таблицы и в интервью жирный шрифт - дальше будет больше). Новости разбиваются по разделам для сортированного вывода. 

В настройках возможно включить вывод только непрочитанных новостей (после просмотра новость больше не выводится в списке - если настройку отключить, выводятся все новости). Вывод новостей обычным списком или списком карточек выбирается в настройках.

Имеется возможность включить обновление по расписанию с выбором интервала обновления.

***
<b>В проекте были использованы</b>

Fragments, RecyclerView, CardView, ViewPager, Google Analytics, Animations, Content Provider, CursorLoader, AsyncTask, Service, BroadCast Receiver, Support Library, Jsoup (работа с html, xml, json), Glide, Preference Fragment, SQLite.

***
<b>Актуальный релиз</b>

F1 News reader v.1.8 (https://github.com/orloffski/F1News.ru-reader/releases/tag/1.8)

***
Все релизы доступны: https://github.com/orloffski/F1News.ru-reader/releases

# License
	Copyright 2016 Orlovsky Engen

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

### map/flatMap

Я написал небольшое API для работы с базами данных через jdbc
 [DBRes.scala](src/main/scala/fintech/homework08/DBRes.scala)
и использовал его для написания [PeopleApp](src/main/scala/fintech/homework08/PeopleApp.scala)

К сожалению когда я запускаю приложение я вижу что оно коннектится к базе много раз

Добавьте в DBRes методы map и flatMap и перепешите код используя for
и выполняя execute только один раз

# Тестовое Задание: Создать ранжирование покерных рук (набор карт игрока).

**Используемые средства:**
- Java
- Gradle для сборки и управления зависимостями
- Для тестирования используется JUnit 5

**Референсные материалы:** правила Техасского холдема https://en.wikipedia.org/wiki/Texas_hold_%27em

## Описание
1. **Реализовать класс для ранжирования покерных рук.**

Класс должен содержать конструктор, который принимает на вход строку, содержащую пять карт:

PokerHand hand = new PokerHand("KS 2H 5C JD TD");

Характеристики входной строки:  

•	В качестве разделителя используется пробел.  
•	Описание каждой карты состоит из двух символов:  
•	первый символ — это номинал карты. Допустимые значения: 2, 3, 4, 5, 6, 7, 8, 9, T(en), J(ack), Q(ueen), K(ing), A(ce);  
•	второй символ — масть. Допустимые значения: S(pades), H(earts), D(iamonds), C(lubs).  

2.	**Реализовать возможность сортировки рук по «силе» (рейтингу / рангу) от сильной к слабой:**

ArrayList<PokerHand> hands = new ArrayList<PokerHand>();  
hands.add(new PokerHand("KS 2H 5C JD TD"));  
hands.add(new PokerHand("2C 3C AC 4C 5C"));
Collections.sort(hands);  

3. **Класс PokerHand должен быть покрыт unit-тестами (определение комбинаций и сравнение комбинаций).**

## Установка и запуск проекта

Для того чтобы установить и запустить проект, выполните следующие шаги:

## Шаг 1: Клонирование репозитория

Сначала нужно клонировать репозиторий на свой локальный компьютер:
```
git clone https://github.com/arinaefanova/poker-test-task.git
```

## Шаг 2: Перейти в каталог проекта

После того как репозиторий будет склонирован, перейдите в каталог проекта:
```
cd poker-test-task
 ```

## Шаг 3: Установка зависимостей

Проект использует Gradle для управления зависимостями и сборки. Для того чтобы установить все необходимые зависимости, 
выполните одну из команд в зависимости от вашей операционной системы.

**Для macOS и Linux:**
```
./gradlew build
 ```
**Для Windows:**
```
gradlew.bat build
 ```
Эта команда скачает все зависимости и создаст структуру проекта.

## Шаг 4: Запуск проекта

После того как все зависимости установлены, вы можете запустить проект. Для этого выполните одну из следующих команд.

**Для macOS и Linux:**
```
./gradlew run
 ```
**Для Windows:**
```
gradlew.bat run
 ```
Эта команда запустит проект.

## Шаг 5: Запуск тестов

Проект использует JUnit 5 для тестирования. Для того чтобы запустить все тесты, выполните одну из следующих команд.

**Для macOS и Linux:**
```
./gradlew test
 ```
**Для Windows:**
```
gradlew.bat test
 ```
Эта команда выполнит все тесты, настроенные в этом проекте.
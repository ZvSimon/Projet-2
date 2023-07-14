# Building a ZIO Application Backend

## Topic

Building a REST API using the "Major League Baseball Dataset"
from [Kaggle](https://www.kaggle.com/datasets/saurabhshahane/major-league-baseball-dataset).

### Dataset Description

The "Major League Baseball Dataset" from Kaggle is a comprehensive collection of data related to Major League Baseball (
MLB) games, players, teams, and statistics. The dataset contains information about game-by-game Elo ratings and
forecasts back to 1871. You can visit the Kaggle page for a more detailed description of the dataset.

The dataset is available in CSV format: `mlb_elo.csv` contains all data: `mlb_elo_latest.csv` contains data for only the
latest season.

### Ratings Systems: ELO and MLB Predictions

The dataset includes two ratings systems, ELO and MLB Predictions, which are used to evaluate teams' performance and
predict game outcomes:

1. **ELO**: The ELO rating system is a method for calculating the relative skill levels of teams in two-player games,
   such as chess. In the context of MLB, the ELO rating system assigns a numerical rating to each team, which reflects
   their relative strength. The rating is updated based on game outcomes, with teams gaining or losing points depending
   on the result of the match.

2. **MLB Predictions**: The MLB Predictions rating system utilizes various statistical models and algorithms to predict
   game outcomes. It takes into account factors such as team performance, player statistics, historical data, and other
   relevant factors to generate predictions for upcoming games.

# USAGE

## ZIO Http: Building HTTP Applications

### Resources

Using ZIO and related libraries: Build application backend using Scala 3 and leverage the power of ZIO. Utilizing
libraries such as `zio-jdbc`, `zio-streams`, `zio-json`, or `zio-http` to handle database operations, stream processing,
JSON parsing, and HTTP application, respectively.

Database initialization at startup: Implementing a mechanism to initialize the H2 database engine at application
startup. Using ZIO for managing the initialization process and setting up the required database schema. To process CSV,
you can use the [tototoshi/scala-csv](https://github.com/tototoshi/scala-csv) library.

See documentation links:

* [ZIO Http](https://zio.dev/zio-http/)
* [ZIO JDBC](https://zio.dev/zio-jdbc/)
* [ZIO Worksheet](https://github.com/dacr/zio-worksheet)
* [tototoshi/scala-csv](https://github.com/tototoshi/scala-csv)
* Using [sbt-revolver](https://github.com/spray/sbt-revolver) in workflow.
  Adding following dependency to `project/plugins.sbt`:

```scala
addSbtPlugin("io.spray" % "sbt-revolver" % "0.10.0")
```

Adding following dependency to `build.sbt` :

```scala
 enablePlugins(RevolverPlugin)
```

### Usage

Compile, then run project with auto reload thanks to sbt-revolver:

``` bash
$ sbt run
```

#### Method.POST :

```bash
$ curl  http://localhost:8080/gameHistory -d {'"team": "SDP", "startYear": "2019", "endYear": "2021"'}
```

#### Method.GET for static :

Test a query with: ( Response text "Hello World!" )

```bash
$ curl http://localhost:8080/text 
```

Test a query with: ( Response json {"greetings": "Hello World!"}  )

```bash
$ curl http://localhost:8080/json
```

#### Method.GET for endpoints :

Prediction of the last game to be played . ATL(HomeTeam) / NYM (AwayTeam) :

```bash
$ curl http://localhost:8080/predict/game/ATL/NYM
```

Last played game of ATL(HomeTeam) and NYM (AwayTeam) :

```bash
$ curl http://localhost:8080/history/game/ATL/NYM
```

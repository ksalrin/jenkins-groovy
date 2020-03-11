node("master") {
    stage("Pull code") {
        checkout scm
    }
}
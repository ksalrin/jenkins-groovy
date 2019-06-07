import java.net.HttpURLConnection;

Object sendMessage(String type, String message, String status, String channel) {
  String color        = ""
  String slackUrl     = "https://fuchicorp.slack.com/services/hooks/jenkins-ci/"
  String slackToken   = System.getenv("SLACK_API_TOKEN")
  if (!channel.contains("#")) {
    channel = "#" + channel
  }

  switch(status) {
    case "SUCCESS":
      color = "#00FF00"
      break

    case "FAILURE":
      color = "#FF0000"
      break

    default:
      color = "#FFFF00"
      break
  }

  switch(type) {
    case "slack":
      slackSend(channel: channel, color: color, baseUrl: slackUrl, tokenCredentialId: slackToken, message: message)
      break
    default:
      println("No default notification system please use slack")
      break
  }

  println("""
  status:  ${status},
  type:    ${type},
  color:   ${color},
  message: ${message}
  """)
}


Object slackSend(object = [ Map channel, Map color, Map baseUrl, Map slackToken, Map message]) {

  println(object)

}


sendMessage('slack', 'The Jenkins job successfully finished deployment.', 'SUCCESS', '#devops')

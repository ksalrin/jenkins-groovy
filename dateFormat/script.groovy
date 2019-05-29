import java.text.SimpleDateFormat


def tagForGit = ""

def dateFormat = new SimpleDateFormat("yyyy-MM-DD'T'HH:m:s.S")
String timeStamp = new SimpleDateFormat("yyyy/MM/dd.HH-mm-ss").format(Calendar.getInstance().getTime())

tagForGit = "Todays date ${timeStamp}"
println(tagForGit)

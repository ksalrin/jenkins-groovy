def git_user = 'fsadykov'
def git_repo = 'onelick'




println(""" 
curl  -d '{ "git_user" : "${git_user}" }'
The repo was selected: ${git_repo}
""")
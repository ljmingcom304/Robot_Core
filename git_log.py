coimport os
import sys

#! 输出的日志格式
pretty = "--pretty=format:\"提交:%Cred%h%Creset%C(yellow)%d%Creset %n作者:%C(bold blue)%an%Creset %n日期:%Cgreen%cd %Creset(%cr) %n记录:%s %n\""
if sys.argv[1] == "-s":
    pretty = "--pretty=format:%s"

#! 输出的日期格式
date = "--date=format:\"%Y-%m-%d %H:%M:%S\""

#! 最近的提交记录
since = "--since=60.days"

#! 重定向日志文件
file = "git_log.txt"

result = os.system("git log --all --abbrev-commit "+pretty+" "+date+" "+since+">"+file)

if result==0:
    print("Git日志输出成功["+file+"]")
else:
    print("Git日志输出失败")
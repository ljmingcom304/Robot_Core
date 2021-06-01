import os
import re
import configparser
import sys

config_file = "git_commit.ini"
config_git = "git"
config = configparser.ConfigParser()
log_file = "git_log.txt"

def writeConfig(type,code,desc):
    config.set(config_git, "type", type)
    config.set(config_git, "code", code)
    config.set(config_git, "desc", desc)
    with open(config_file, 'w', encoding="utf-8") as f:
        config.write(f)

def getCount(message):
	count = 0
	os.system("git log --all --pretty=format:\"%s\" >"+log_file)
	with open(log_file,"r", encoding="utf-8") as f:
		for line in f.readlines():
			regex = re.search(r"^"+message,line)
			if regex:
				count += 1
	with open(log_file,"w", encoding="utf-8") as f:
		f.truncate()
	return count
		

def parseCommit():
	config.read(config_file,"utf-8")
	type = config.get(config_git,"type")
	code = config.get(config_git,"code")
	desc = config.get(config_git,"desc")

	if not type == "DEV" and not type == "BUGFIX" and not type == "STORY":
		print("提交类型错误："+type+"[正确类型为DEV(一般提交)或BUGFIX(bug修复)或STORY(任务需求)]")
		return
	if not type == "DEV" and (not code or not code.isdigit()):
		print("任务编号必须为数字")
		return
	if not desc:
		print("修改描述不能为空")
		return

	message = type;

	if code:
		message += "#"+code
	count = 0
	if not type == "DEV":
		count = str(getCount(message)+1)
		message += "#"+count
	message+=" "+desc
	print(message)

	# 提交代码
	writeConfig("","","")
	os.system("git add .")
	print("COMMIT==>代码向工作区提交")
	result = os.system("git commit -m \""+message+"\"")

	if result == 0:
	    if len(sys.argv) == 2 and sys.argv[1] == "-p":
	        print("COMMIT==>代码向远程服务器同步")
	        result = os.system("git push")
	        if not result == 0:
	            print("COMMIT==>代码同步服务器失败")
	else:
	    writeConfig(type,code,desc)
	    print("COMMIT==>代码提交失败")

fetch = os.popen("git fetch").read()
if not fetch.strip():
    parseCommit()
else:
    i = input("COMMIT==>是"+fetch+"否合并远程服务器最新代码(Y/N):")
    if i == "y" or i == "Y":
        os.system("git merge")
        print("COMMIT==>合并远程代码到本地，若不存在冲突请重新提交")
    else:
        print("COMMIT==>取消远程服务器代码向本地合并")


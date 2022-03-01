# nft-bigdata

#### 介绍
大数据在区块链数据分析领域的应用-api服务

#### 软件架构
![输入图片说明](https://images.gitee.com/uploads/images/2022/0301/155339_678ddc5d_8689480.jpeg "jiagoutu.jpg")


#### 安装教程

1.  安装jenkins

``docker run \
  -u root \
  -d \
  -p 8080:8080 \
  -p 50000:50000 \
  -v jenkins-data:/var/jenkins_home \
  -v /etc/localtime:/etc/localtime:ro \
  -v /var/run/docker.sock:/var/run/docker.sock \
  --restart=always \
  jenkinsci/blueocean:1.24.1``
  
2.  安装docker
3.  使用Jenkins流水线构建部署

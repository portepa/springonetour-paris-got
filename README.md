# Demo

## Deploy a K8S cluster

```
pks create-cluster cluster -e cluster.pap.kewstom.com -p small
```

If you don't use NSX-T, make sure to create a load balancer to point to your deployed master.

## Connect to PKS K8S cluster 

```sh
pks get-credentials <my-cluster>

# verify that it's working fine

kubectl get nodes
```

## Install tiller and helm

We first need to install the roles and RBAC for Tiller, then install it with helm

```sh
kubectl --namespace kube-system create serviceaccount tiller

kubectl create clusterrolebinding tiller-cluster-rule \
 --clusterrole=cluster-admin --serviceaccount=kube-system:tiller

kubectl --namespace kube-system patch deploy tiller-deploy \
 -p '{"spec":{"template":{"spec":{"serviceAccount":"tiller"}}}}' 

helm init
```

## Install PostgreSQL

```
helm install --name my-postgres -f helm-db-conf.yml stable/postgresql
```

## Deploy the service broker

You need to wait for the ip of the service to spawn, then modify the `service-broker.yml`.

```sh
# fetch the ip of the svc exposing postgres
kubectl get svc my-postgres-postgresql -o=jsonpath='{.status.loadBalancer.ingress[0].ip}'
# change the ip in the service broker file
vi +22 service-broker.yml
# deploy it
kubectl apply -f service-broker.yml
```

## Log-in to Cloud Foundry

```sh
# put the right credentials needed for your PCF
cf login -a api.run.pcfone.io --sso
```

## Create the service broker on PAS Marketplace

```sh
# First get the ip of the service broker
cf create-service-broker psql-spring-one-service-broker \
  admin easypassword http://$(kubectl get svc psql-service-broker -o=jsonpath='{.status.loadBalancer.ingress[0].ip}') --space-scoped
```

## Deploy the back-end of our app on PAS

```
cd demo-s1t-paris
cf push --no-start
```

## Attach a service

Suggest to do this via the GUI, cooler and allows to show the UI and start the app directly after.

```
cf create-service psql-k8s-broker-id standard my-psql-service-instanced
cf bind-service s1t-paris-backend my-psql-service-instanced
```

## Start the app

```
cf start
```
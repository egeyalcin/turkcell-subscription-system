mvn clean install

docker build --build-arg JAR_FILE=subscription-service/target/*.jar -t subscription-service .
docker build --build-arg JAR_FILE=payment-service/target/*.jar -t payment-service .
docker build --build-arg JAR_FILE=turkcell-gateway-service/target/*.jar -t gateway-service .
docker build --build-arg JAR_FILE=notification-service/target/*.jar -t notification-service .
docker build --build-arg JAR_FILE=config-server/target/*.jar -t config-server .

docker pull confluentinc/cp-zookeeper:7.5.0

docker pull confluentinc/cp-kafka:7.5.0

minikube delete --all --purge
minikube start --driver=docker

minikube addons enable metrics-server

kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/rbac.yaml

eval $(minikube docker-env)

minikube image load subscription-service:latest
minikube image load gateway-service:latest
minikube image load config-server:latest
minikube image load notification-service:latest
minikube image load payment-service:latest

minikube image load confluentinc/cp-kafka:7.5.0
minikube image load confluentinc/cp-zookeeper:7.5.0

kubectl apply -f k8s/postgres/postgres-storage.yaml
kubectl apply -f k8s/postgres/postgres-deployments.yaml
kubectl apply -f k8s/postgres/postgres-services.yaml

kubectl apply -f k8s/zookeeper/
kubectl apply -f k8s/kafka/
kubectl apply -f k8s/config-server/ -n turkcell
kubectl apply -f k8s/notification-service/ -n turkcell
kubectl apply -f k8s/payment-service/ -n turkcell
kubectl apply -f k8s/subscription-service/ -n turkcell
kubectl apply -f k8s/gateway-service/ -n turkcell

kubectl autoscale deployment config-server --cpu-percent=50 --min=1 --max=4 -n turkcell
kubectl autoscale deployment subscription-service --cpu-percent=50 --min=1 --max=8 -n turkcell
kubectl autoscale deployment payment-service --cpu-percent=50 --min=1 --max=6 -n turkcell
kubectl autoscale deployment notification-service --cpu-percent=50 --min=1 --max=4 -n turkcell
kubectl autoscale deployment gateway-service --cpu-percent=50 --min=1 --max=6 -n turkcell

echo "Deployment is successfully!!"

kubectl port-forward svc/gateway-service 8016:8016 -n turkcell
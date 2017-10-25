package com.viettel.akka.actors;

import akka.actor.AllForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import com.viettel.akka.beans.AkkaTask;
import com.viettel.fw.Exception.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.stop;

@Component
@Scope("prototype")
public class TaskActor extends UntypedActor {
    private final LoggingAdapter log = Logging
            .getLogger(getContext().system(), "TaskActor");


    @Autowired
    ApplicationContext context;

    public static class Get {
    }

    public static class NULL {

    }

    Object object = null;

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new AllForOneStrategy(1, Duration.create(10, TimeUnit.MINUTES),
                new Function<Throwable, SupervisorStrategy.Directive>() {
                    @Override
                    public SupervisorStrategy.Directive apply(Throwable param) throws Exception {
                        if (param instanceof Exception) {
                            return stop();
                        } else {
                            return escalate();
                        }
                    }
                }
        );
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Get) {
            if (object == null) {
                getSender().tell(new NULL(), getSelf());

            } else {
                getSender().tell(object, getSelf());
            }
            return;
        }
        AkkaTask akkaTask = (AkkaTask) message;
        try {
            String serviceName = akkaTask.getServiceName();
            String methodName = akkaTask.getMethodName();
            Class className = akkaTask.getClassName();
            Object bean = akkaTask.getBean();
            if (className != null && serviceName != null && bean == null) {
                throw new Exception("Set value for only one of service,class or bean !");
            }
            if (className == null && serviceName == null && bean == null) {
                throw new Exception("Service name,class name or bean must not be null!");
            }
            if (bean == null) {
                if (serviceName != null) {
                    bean = context.getBean(serviceName);
                } else {
                    bean = context.getBean(className);
                }
            }

            Object[] parameters = akkaTask.getParameters();
            Class[] parameterTypes = null;
            int i = 0;
            java.lang.reflect.Method method = null;
            if (parameters != null) {
                parameterTypes = new Class[parameters.length];
                for (Object ob : parameters) {
                    parameterTypes[i] = ob.getClass();
                    i++;
                }
                method = bean.getClass().getMethod(methodName, parameterTypes);
            } else {
                method = bean.getClass().getMethod(methodName);
            }
            object = method.invoke(bean, parameters);

        } catch (InvocationTargetException ite) {
            object = new Exception(ite.getCause());
        }

    }
}

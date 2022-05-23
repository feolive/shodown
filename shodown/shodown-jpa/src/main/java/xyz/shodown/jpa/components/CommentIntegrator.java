package xyz.shodown.jpa.components;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.jpa.annotation.Comment;

import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * @description: 解析注释
 * @author: wangxiang
 * @date: 2022/4/17 15:38
 */
@Slf4j(topic = LogCategory.PLATFORM)
public class CommentIntegrator implements Integrator {

    public static final CommentIntegrator INSTANCE = new CommentIntegrator();

    public CommentIntegrator() {
        super();
    }

    /**
     * Perform comment integration.
     *
     * @param metadata        The "compiled" representation of the mapping information
     * @param sessionFactory  The session factory being created
     * @param serviceRegistry The session factory's service registry
     */
    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        processComment(metadata);
    }

    /**
     * Not used.
     *
     * @param sessionFactoryImplementor     The session factory being closed.
     * @param sessionFactoryServiceRegistry That session factory's service registry
     */
    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
    }

    /**
     * Process comment annotation.
     *
     * @param metadata process annotation of this {@code Metadata}.
     */
    private void processComment(Metadata metadata) {
        for (PersistentClass persistentClass : metadata.getEntityBindings()) {
            // Process the Comment annotation is applied to Class
            Class<?> clz = persistentClass.getMappedClass();
            if (clz.isAnnotationPresent(Comment.class)) {
                Comment comment = clz.getAnnotation(Comment.class);
                persistentClass.getTable().setComment(comment.value());
            }

            // Process Comment annotations of identifier.
            Property identifierProperty = persistentClass.getIdentifierProperty();
            if (identifierProperty != null) {
                fieldComment(persistentClass, identifierProperty.getName());
            } else {
                org.hibernate.mapping.Component component = persistentClass.getIdentifierMapper();
                if (component != null) {
                    //noinspection unchecked
                    Iterator<Property> iterator = component.getPropertyIterator();
                    while (iterator.hasNext()) {
                        fieldComment(persistentClass, iterator.next().getName());
                    }
                }
            }
            // Process fields with Comment annotation.
            //noinspection unchecked
            Iterator<Property> iterator = persistentClass.getPropertyIterator();
            while (iterator.hasNext()) {
                fieldComment(persistentClass, iterator.next().getName());
            }
        }
    }

    /**
     * Process @{code comment} annotation of field.
     *
     * @param persistentClass Hibernate {@code PersistentClass}
     * @param columnName      name of field
     */
    private void fieldComment(PersistentClass persistentClass, String columnName) {
            Field field = findFieldRecursively(persistentClass.getMappedClass(),columnName);
            if(field==null){
                return;
            }
            if (field.isAnnotationPresent(Comment.class)) {
                String comment = field.getAnnotation(Comment.class).value();
                String sqlColumnName = persistentClass.getProperty(columnName).getValue().getColumnIterator().next().getText();
                Iterator<org.hibernate.mapping.Column> columnIterator = persistentClass.getTable().getColumnIterator();
                while (columnIterator.hasNext()) {
                    org.hibernate.mapping.Column column = columnIterator.next();
                    if (sqlColumnName.equalsIgnoreCase(column.getName())) {
                        column.setComment(comment);
                        break;
                    }
                }
            }
    }

    /**
     * 递归查找属性
     *
     * @param clazz
     * @param cloumnName
     * @return
     */
    private Field findFieldRecursively(Class<?> clazz, String cloumnName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(cloumnName);
        } catch (NoSuchFieldException | SecurityException ignored) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                field = findFieldRecursively(superclass, cloumnName);
            }
        }
        return field;
    }
}

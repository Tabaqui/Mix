package ru.motleycrew.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> id;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, String> hash;
	public static volatile SingularAttribute<User, String> token;

}


package ru.motleycrew.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Data.class)
public abstract class Data_ {

	public static volatile SingularAttribute<Data, Date> date;
	public static volatile SingularAttribute<Data, Boolean> approved;
	public static volatile SingularAttribute<Data, String> id;
	public static volatile SingularAttribute<Data, String> text;
	public static volatile SingularAttribute<Data, String> title;
	public static volatile ListAttribute<Data, User> users;

}


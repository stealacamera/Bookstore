module BookstoreRE {
	requires javafx.base;
	requires javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.media;
    requires javafx.web;
	requires java.desktop;
	
	requires junit;
	requires org.junit.jupiter.api;
	
	exports startup;
    exports utils;
    exports exceptions;
	
    exports views;
    exports views.employees;
    exports views.books;
    exports views.stats;
    
    exports controllers;
    exports controllers.books;
    
    exports models;
    exports models.utilities;
    
    exports dal;
    exports dal.IRepositories;
    
    exports bll;
    exports bll.IServices;
    exports bll.dto;
    
    exports test;
}
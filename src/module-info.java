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
    exports views.statistics;
    
    exports controllers;
    
    exports dal;
    exports dal.IRepositories;
    exports dal.models;
    exports dal.models.utilities;
    
    exports bll;
    exports bll.IServices;
    exports bll.dto;
    
    exports test;
}
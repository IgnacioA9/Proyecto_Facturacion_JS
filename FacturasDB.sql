CREATE DATABASE Facturas;

USE Facturas;

CREATE TABLE Administradores(
    numeroid int AUTO_INCREMENT,
	cedula varchar(20) NOT NULL,
	nombre varchar(30) NOT NULL,
	
	CONSTRAINT pkAdm PRIMARY KEY (numeroid),
    CONSTRAINT uniqueAdm UNIQUE (cedula)
);

CREATE TABLE Usuarios(
    numeroid int AUTO_INCREMENT,
	identificacion varchar(20) NOT NULL,
	contrasena varchar(20) NOT NULL,
	rol varchar(10) NOT NULL,
	
	CONSTRAINT pkUsuario PRIMARY KEY (numeroid),
    CONSTRAINT uniqueUsuarios UNIQUE (identificacion),
	CONSTRAINT verify CHECK (rol IN ('ADMIN', 'PROVEE'))
);

CREATE TABLE Proveedores(
    numeroid int AUTO_INCREMENT,
	cedula varchar(20) NOT NULL,
	nombre varchar(30) NOT NULL,
	correo varchar(30),
	telefono varchar(20),
	estado bool NOT NULL,
	
	CONSTRAINT pkProvee PRIMARY KEY (numeroid),
    CONSTRAINT uniqueProvee UNIQUE (cedula)
);

CREATE TABLE Clientes(
	numeroid int AUTO_INCREMENT,
	cedula varchar(20) NOT NULL,
	nombre varchar(30) NOT NULL,
	correo varchar(30),
	telefono varchar(20),
	
	CONSTRAINT pkClientes PRIMARY KEY (numeroid),
	CONSTRAINT uniqueCedula UNIQUE (cedula)
);

CREATE TABLE Facturas(
	numero int AUTO_INCREMENT,
	cantidad int,
	monto float,
	fecha date NOT NULL,
	
	CONSTRAINT pkFact PRIMARY KEY (numero)
);

CREATE TABLE Productos(
	numeroid int AUTO_INCREMENT,
	codigo varchar(20) NOT NULL,
	nombre varchar(30) NOT NULL,
	precio float NOT NULL,
	
	CONSTRAINT pkPro PRIMARY KEY (numeroid),
	CONSTRAINT uniqueCod UNIQUE (codigo)
);

CREATE TABLE Posee(
	numeroprovee int,
	numeroclien int,
	
	CONSTRAINT pkPosee PRIMARY KEY (numeroclien),
	CONSTRAINT fk1Posee FOREIGN KEY (numeroclien) REFERENCES Clientes(numeroid),
	CONSTRAINT fk2Posee FOREIGN KEY (numeroprovee) REFERENCES Proveedores(numeroid)
);

CREATE TABLE Tiene(
	numeroprovee int,
	numerofac int,
	
	CONSTRAINT pkTiene PRIMARY KEY(numerofac),
	CONSTRAINT fk1Tiene FOREIGN KEY(numerofac) REFERENCES Facturas (numero),
	CONSTRAINT fk2Tiene FOREIGN KEY(numeroprovee) REFERENCES Proveedores (numeroid)
);

CREATE TABLE Contiene(
	numeroprod int,
	numerofac int,
	cantidadproducto int,
	
	CONSTRAINT pkContiene PRIMARY KEY (numeroprod, numerofac),
	CONSTRAINT fk1Contiene FOREIGN KEY (numeroprod) REFERENCES Productos (numeroid),
	CONSTRAINT fk2Contiene FOREIGN KEY (numerofac) REFERENCES Facturas (numero)
);

CREATE TABLE Almacena(
	numeroprovee int,
	numeroprod int,
	
	CONSTRAINT pkAlmacena PRIMARY KEY (numeroprod),
	CONSTRAINT fk1Almacena FOREIGN KEY (numeroprovee) REFERENCES Proveedores(numeroid),
	CONSTRAINT fk2Almacena FOREIGN KEY (numeroprod) REFERENCES Productos(numeroid)
);

CREATE TABLE Adquiere(
	numeroclien int,
	numerofac int,
	CONSTRAINT pkAdquiere PRIMARY KEY (numerofac),
	CONSTRAINT fk1Adquiere FOREIGN KEY (numerofac) REFERENCES Facturas(numero),
	CONSTRAINT fk2Adquiere FOREIGN KEY (numeroclien) REFERENCES Clientes(numeroid)
);



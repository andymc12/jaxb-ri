/*
 * Copyright (c) 1997, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import java.io.FileInputStream;
import java.io.IOException;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

// import java content classes generated by binding compiler
import extend.*;

/*
 * $Id: Main.java,v 1.1 2007-12-05 00:49:39 kohsuke Exp $
 */
 
public class Main {
    // This sample application demonstrates how to modify a java content
    // tree and marshal it back to a xml data
    
    public static void main( String[] args ) {
	final boolean enlightened = true;

        try {
            // create a JAXBContext capable of handling classes generated into
            // the org.example package
            JAXBContext jc = JAXBContext.newInstance( "extend" );
            
            // create an Unmarshaller
            Unmarshaller u = jc.createUnmarshaller();
            
            // unmarshal a po instance document into a tree of Java content
            // objects composed of classes from the primer.po package.
            Itinerary it = 
                (Itinerary)u.unmarshal( new FileInputStream( "itinerary.xml" ) );

            ObjectFactory of = new ObjectFactory();
	        java.util.Iterator iter = it.getTravel().listIterator();

            if (enlightened) {
	            System.out.println("Process references using polymorphic method");
            } else {
	            System.out.println("Process references using forest of if-then-else :(");
            }
	    for(int i=1; iter.hasNext(); i++) {
	        // Travel Entry Header
 	        System.out.println("****************************");
		    System.out.println("[" + i + "]");

	        // Process travel entry
            JAXBElement jxbe = (JAXBElement)iter.next();
   	        TravelType travel = (TravelType)jxbe.getValue();
	        if (enlightened) {
	          ((TravelTypeExtend)travel).printTravelSummary();

                  Marshaller m = jc.createMarshaller();
                  m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
                  m.marshal( of.createTravel(travel), System.out );

                } else {
		  // Proceed down the forest of Ifs-then-elses.
  	          System.out.println("Origin=" + travel.getOrigin());
	          System.out.println("Destination=" + travel.getDestination());
		  if (travel instanceof PlaneType) {
	             PlaneType planet= (PlaneType)travel;
	             System.out.println("Flight Number: " +
					 planet.getFlightNumber());
	             System.out.println("Meal: " + planet.getMeal());

                  } else if (travel instanceof AutoType) {
	             AutoType auto= (AutoType)travel;
	             System.out.println("Rental Agency:" + auto.getRentalAgency());
   	             System.out.println("Rate Per Hour:" + auto.getRatePerHour());
                  } else if (travel instanceof TrainType) {
	             TrainType train= (TrainType)travel;
                     System.out.println("Track: " + train.getTrack());
                     System.out.println("Schedule# " + 
				train.getDailyScheduleNumber());
                  }
               }
              // Travel Entry footer
              System.out.println("****************************");
	        System.out.println();
            }

            // create a Marshaller and marshal to a file
            Marshaller m = jc.createMarshaller();
            m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            m.marshal( it, System.out );
            
        } catch( JAXBException je ) {
            je.printStackTrace();
        } catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }
}

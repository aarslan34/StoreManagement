package com.arslan.store.serviceimpl;

import com.arslan.store.constants.StoreConstants;
import com.arslan.store.dao.BillDao;
import com.arslan.store.model.Bill;
import com.arslan.store.service.BillService;
import com.arslan.store.utils.StoreUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    BillDao billDao;
    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Inside generateReport");
        try {
            log.info("tryStart");
            String fileName;
            log.info("Value of validateRequestMap(requestMap)"+String.valueOf(validateRequestMap(requestMap)));
            if(validateRequestMap(requestMap)){
                log.info("if(validateRequestMap(requestMap)) of generateReport");
                if(requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")){
                    fileName = (String) requestMap.get("uuid");
                }else{
                    log.info("else(validateRequestMap(requestMap)) of generateReport");
                    fileName = StoreUtils.getUUID();
                    requestMap.put("uuid", fileName);

                    insertBill(requestMap);

                }

                log.info("1");

                String data = "Name: " + requestMap.get("fileName") + "\n" + "Contact Number: "
                        + requestMap.get("contactNumber") + "\n" + "Email: " + requestMap.get("email")
                        + "\n" + "Payment Method: " + requestMap.get("paymentMethod");

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(StoreConstants.STORE_LOCATION +"\\" + fileName + ".pdf"));

                document.open();
                setRectangleInPdf(document);

                Paragraph chunk = new Paragraph("Store Management System", getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph = new Paragraph(data + "\n \n", getFont("Data"));
                document.add(paragraph);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JSONArray jsonArray = StoreUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
                log.info("jsonarray: " +jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++){
                    addRows(table, StoreUtils.getMapFromJson(jsonArray.getString(i)));
                }

                document.add(table);

                Paragraph footer = new Paragraph("Total: " + requestMap.get("totalAmount") + "\n"
                + "Thank you for visiting. Please come again!");

                document.add(footer);
                document.close();

                log.info("2");
                return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);
                
                
            }

            return StoreUtils.getResponseEntity("Required data not found", HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return StoreUtils.getResponseEntity(StoreConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {

        table.addCell((String)data.get("name"));
        table.addCell( (String) data.get("category"));
        table.addCell( Double.toString((Double) data.get("quantity")));
        table.addCell(Double.toString ((Double) data.get("price")));
        table.addCell( Double.toString((Double)  data.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("inside addTableHeader");
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type){
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;

            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.WHITE);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Inside setRectangleInPdf");
        Rectangle rectangle = new Rectangle(577, 825, 18, 15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(3);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBackgroundColor(BaseColor.WHITE);
        rectangle.setBorderWidth(1);
        document.add(rectangle);
    }

    private void insertBill(Map<String, Object> requestMap) {

        log.info("inside insertBill");
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String)requestMap.get("fileName"));
            bill.setEmail((String)requestMap.get("email"));
            bill.setContactNumber((String)requestMap.get("contactNumber"));
            bill.setPaymentMethod((String)requestMap.get("paymentMethod"));
            bill.setTotal(Integer.parseInt((String)requestMap.get("totalAmount")));
            bill.setProductDetails((String) requestMap.get("productDetails"));
            bill.setCreatedBy("Admin");
            billDao.save(bill);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
//        log.info("inside validateRequestMap");
//        log.info(String.valueOf(requestMap.containsKey("fileName")));
//        log.info(String.valueOf(requestMap.containsKey("contactNumber")));
//        log.info(String.valueOf(requestMap.containsKey("email")));
//        log.info(String.valueOf(requestMap.containsKey("paymentMethod")));
//        log.info(String.valueOf(requestMap.containsKey("productDetails")));
//        log.info(String.valueOf(requestMap.containsKey("totalAmount")));
//        boolean b = requestMap.containsKey("fileName") &&
//                requestMap.containsKey("contactNumber") &&
//                requestMap.containsKey("email") &&
//                requestMap.containsKey("paymentMethod") &&
//                requestMap.containsKey("productDetails") &&
//                requestMap.containsKey("totalAmount");
//        log.info("overall validate from method " + String.valueOf(b));

        return requestMap.containsKey("fileName") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("totalAmount");
    }
}

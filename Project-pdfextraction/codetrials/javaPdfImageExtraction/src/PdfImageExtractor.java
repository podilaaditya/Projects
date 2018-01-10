

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.lang.String;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;

import javax.imageio.ImageIO;


@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class PdfImageExtractor {

    public static void main(String[] args) {
        try {

            String sourceDir = "/Volumes/Data/Work/GitHub/Projects/Project-pdfextraction/codetrials/files/BAL-MO-NCR-HARYANA.pdf";// Paste pdf files in PDFCopy folder to read
            String destinationDir = "C:/PDFCopy/";
            File oldFile = new File(sourceDir);
            if (oldFile.exists()) {
                PDDocument document = PDDocument.load(oldFile);
                List<BufferedImage> lImageList =  getImagesFromPDF(document);
                System.out.println("lImageList Count = " + lImageList.size());
                saveImagefiles(lImageList);
            }
            else {
                System.err.println("File not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static  List<BufferedImage>   getImagesFromPDF(PDDocument document) throws IOException {
        List<BufferedImage> images = new ArrayList<>();
        for (PDPage page : document.getPages()) {
            images.addAll(getImagesFromResources(page.getResources()));
        }

        return images;
    }

    private  static List<BufferedImage> getImagesFromResources(PDResources resources) throws IOException {
        List<BufferedImage> images = new ArrayList<>();

        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);

            if (xObject instanceof PDFormXObject) {
                images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
            } else if (xObject instanceof PDImageXObject) {
                images.add(((PDImageXObject) xObject).getImage());
            }
        }

        return images;
    }

    private static void saveImagefiles(List<BufferedImage> images) {

        try {
            int Cntr = 0;
            for(BufferedImage Element : images) {
                String lFileName = "/Volumes/Data/Work/GitHub/Projects/Project-pdfextraction/codetrials/files/"+"MyFile"+Cntr+".png";
                System.out.println("lFileName = " + lFileName);

                File fObj = new File(lFileName);
                ImageIO.write(Element, "PNG",fObj);

                Cntr++;
            }
        }
        catch (Exception excepObj) {
            excepObj.printStackTrace();
        }

    }
}

/*

                PDPageTree list = document.getDocumentCatalog().getPages();

                String fileName = oldFile.getName().replace(".pdf", "_cover");
                int totalImages = 1;
                for (PDPage page : list) {
                    PDResources pdResources = page.getResources();

//                    Map pageImages = pdResources.getImages();
//                    if (pageImages != null) {
//
//                        Iterator imageIter = pageImages.keySet().iterator();
//                        while (imageIter.hasNext()) {
//                            String key = (String) imageIter.next();
//                            PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);
//                            pdxObjectImage.write2file(destinationDir + fileName + "_" + totalImages);
//                            totalImages++;
//                        }
//                    }

//                    for (COSName c : pdResources.getXObjectNames()) {
//                        PDXObject o = pdResources.getXObject(c);
//                        if (o instanceof org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) {
//                            File file = new File("D:/Temp/" + System.nanoTime() + ".png");
//                            ImageIO.write(((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject)o).getImage(), "png", file);
//                        }
//                    }

                }
 */
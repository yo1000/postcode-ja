package com.yo1000.postcode.infrastructure;

import com.yo1000.postcode.application.port.PostCsv;
import com.yo1000.postcode.config.AppProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.data.util.CloseableIterator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ResourceZippedCsvFileLoaderTests {
    @Test
    void test_load() throws IOException {
        // Given
        byte[] csv = """
                01101,"060  ","0600000","ホッカイドウ","サッポロシチュウオウク","イカニケイサイガナイバアイ","北海道","札幌市中央区","以下に掲載がない場合",0,0,0,0,0,0
                13101,"100  ","1000000","トウキョウト","チヨダク","イカニケイサイガナイバアイ","東京都","千代田区","以下に掲載がない場合",0,0,0,0,0,0
                14101,"230  ","2300000","カナガワケン","ヨコハマシツルミク","イカニケイサイガナイバアイ","神奈川県","横浜市鶴見区","以下に掲載がない場合",0,0,0,0,0,0
                """.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

        ZipOutputStream zipOut = new ZipOutputStream(bytesOut, StandardCharsets.UTF_8);
        ZipEntry entry = new ZipEntry("utf_ken_all.csv");
        zipOut.putNextEntry(entry);
        zipOut.write(csv);
        zipOut.finish();
        zipOut.closeEntry();

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());

        Resource resource = Mockito.mock(Resource.class);
        Mockito.doReturn(bytesIn).when(resource).getInputStream();

        AppProperties appProps = Mockito.mock(AppProperties.class);
        Mockito.doReturn(resource).when(appProps).getResource();

        ResourceZippedCsvFileLoader<PostCsv, PostCsv> loader = new ResourceZippedCsvFileLoader<>(appProps);

        // When
        try (CloseableIterator<PostCsv> iter = loader.load(row -> row)) {
            // Then
            List<PostCsv> posts = StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                    iter, Spliterator.ORDERED), false).toList();

            Assertions.assertThat(posts.size()).isEqualTo(3);

            Assertions.assertThat(posts.get(0).localGovCode()).isEqualTo("01101");
            Assertions.assertThat(posts.get(0).postcode5()).isEqualTo("060  ");
            Assertions.assertThat(posts.get(0).postcode7()).isEqualTo("0600000");
            Assertions.assertThat(posts.get(0).prefectureName()).isEqualTo("北海道");
            Assertions.assertThat(posts.get(0).municipalityName()).isEqualTo("札幌市中央区");
            Assertions.assertThat(posts.get(0).townAreaName()).isEqualTo("以下に掲載がない場合");
            Assertions.assertThat(posts.get(0).prefectureNameKatakana()).isEqualTo("ホッカイドウ");
            Assertions.assertThat(posts.get(0).municipalityNameKatakana()).isEqualTo("サッポロシチュウオウク");
            Assertions.assertThat(posts.get(0).townAreaNameKatakana()).isEqualTo("イカニケイサイガナイバアイ");
            Assertions.assertThat(posts.get(0).townAreaWithMultiplePostcodes()).isEqualTo("0");
            Assertions.assertThat(posts.get(0).townAreaWithAddressNumbersPerKoaza()).isEqualTo("0");
            Assertions.assertThat(posts.get(0).townAreaWithChome()).isEqualTo("0");
            Assertions.assertThat(posts.get(0).postcodeWithMultipleTownAreas()).isEqualTo("0");
            Assertions.assertThat(posts.get(0).changed()).isEqualTo("0");
            Assertions.assertThat(posts.get(0).changeReason()).isEqualTo("0");

            Assertions.assertThat(posts.get(1).localGovCode()).isEqualTo("13101");
            Assertions.assertThat(posts.get(1).postcode5()).isEqualTo("100  ");
            Assertions.assertThat(posts.get(1).postcode7()).isEqualTo("1000000");
            Assertions.assertThat(posts.get(1).prefectureName()).isEqualTo("東京都");
            Assertions.assertThat(posts.get(1).municipalityName()).isEqualTo("千代田区");
            Assertions.assertThat(posts.get(1).townAreaName()).isEqualTo("以下に掲載がない場合");
            Assertions.assertThat(posts.get(1).prefectureNameKatakana()).isEqualTo("トウキョウト");
            Assertions.assertThat(posts.get(1).municipalityNameKatakana()).isEqualTo("チヨダク");
            Assertions.assertThat(posts.get(1).townAreaNameKatakana()).isEqualTo("イカニケイサイガナイバアイ");
            Assertions.assertThat(posts.get(1).townAreaWithMultiplePostcodes()).isEqualTo("0");
            Assertions.assertThat(posts.get(1).townAreaWithAddressNumbersPerKoaza()).isEqualTo("0");
            Assertions.assertThat(posts.get(1).townAreaWithChome()).isEqualTo("0");
            Assertions.assertThat(posts.get(1).postcodeWithMultipleTownAreas()).isEqualTo("0");
            Assertions.assertThat(posts.get(1).changed()).isEqualTo("0");
            Assertions.assertThat(posts.get(1).changeReason()).isEqualTo("0");

            Assertions.assertThat(posts.get(2).localGovCode()).isEqualTo("14101");
            Assertions.assertThat(posts.get(2).postcode5()).isEqualTo("230  ");
            Assertions.assertThat(posts.get(2).postcode7()).isEqualTo("2300000");
            Assertions.assertThat(posts.get(2).prefectureName()).isEqualTo("神奈川県");
            Assertions.assertThat(posts.get(2).municipalityName()).isEqualTo("横浜市鶴見区");
            Assertions.assertThat(posts.get(2).townAreaName()).isEqualTo("以下に掲載がない場合");
            Assertions.assertThat(posts.get(2).prefectureNameKatakana()).isEqualTo("カナガワケン");
            Assertions.assertThat(posts.get(2).municipalityNameKatakana()).isEqualTo("ヨコハマシツルミク");
            Assertions.assertThat(posts.get(2).townAreaNameKatakana()).isEqualTo("イカニケイサイガナイバアイ");
            Assertions.assertThat(posts.get(2).townAreaWithMultiplePostcodes()).isEqualTo("0");
            Assertions.assertThat(posts.get(2).townAreaWithAddressNumbersPerKoaza()).isEqualTo("0");
            Assertions.assertThat(posts.get(2).townAreaWithChome()).isEqualTo("0");
            Assertions.assertThat(posts.get(2).postcodeWithMultipleTownAreas()).isEqualTo("0");
            Assertions.assertThat(posts.get(2).changed()).isEqualTo("0");
            Assertions.assertThat(posts.get(2).changeReason()).isEqualTo("0");
        }
    }

    @Test
    void test_load_blank() throws IOException {
        // Given
        byte[] csv = """
                ,,"","","","","","","",,,,,,
                """.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

        ZipOutputStream zipOut = new ZipOutputStream(bytesOut, StandardCharsets.UTF_8);
        ZipEntry entry = new ZipEntry("utf_ken_all.csv");
        zipOut.putNextEntry(entry);
        zipOut.write(csv);
        zipOut.finish();
        zipOut.closeEntry();

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());

        Resource resource = Mockito.mock(Resource.class);
        Mockito.doReturn(bytesIn).when(resource).getInputStream();

        AppProperties appProps = Mockito.mock(AppProperties.class);
        Mockito.doReturn(resource).when(appProps).getResource();

        ResourceZippedCsvFileLoader<PostCsv, PostCsv> loader = new ResourceZippedCsvFileLoader<>(appProps);

        // When
        try (CloseableIterator<PostCsv> iter = loader.load(row -> row)) {
            // Then
            List<PostCsv> posts = StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                    iter, Spliterator.ORDERED), false).toList();

            Assertions.assertThat(posts.size()).isEqualTo(1);

            Assertions.assertThat(posts.get(0).localGovCode()).isEqualTo("");
            Assertions.assertThat(posts.get(0).postcode5()).isEqualTo("");
            Assertions.assertThat(posts.get(0).postcode7()).isEqualTo("");
            Assertions.assertThat(posts.get(0).prefectureName()).isEqualTo("");
            Assertions.assertThat(posts.get(0).municipalityName()).isEqualTo("");
            Assertions.assertThat(posts.get(0).townAreaName()).isEqualTo("");
            Assertions.assertThat(posts.get(0).prefectureNameKatakana()).isEqualTo("");
            Assertions.assertThat(posts.get(0).municipalityNameKatakana()).isEqualTo("");
            Assertions.assertThat(posts.get(0).townAreaNameKatakana()).isEqualTo("");
            Assertions.assertThat(posts.get(0).townAreaWithMultiplePostcodes()).isEqualTo("");
            Assertions.assertThat(posts.get(0).townAreaWithAddressNumbersPerKoaza()).isEqualTo("");
            Assertions.assertThat(posts.get(0).townAreaWithChome()).isEqualTo("");
            Assertions.assertThat(posts.get(0).postcodeWithMultipleTownAreas()).isEqualTo("");
            Assertions.assertThat(posts.get(0).changed()).isEqualTo("");
            Assertions.assertThat(posts.get(0).changeReason()).isEqualTo("");
        }
    }

    @Test
    void test_load_closingException() throws IOException {
        // Given
        byte[] csv = """
                01101,"060  ","0600000","ホッカイドウ","サッポロシチュウオウク","イカニケイサイガナイバアイ","北海道","札幌市中央区","以下に掲載がない場合",0,0,0,0,0,0
                """.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

        ZipOutputStream zipOut = new ZipOutputStream(bytesOut, StandardCharsets.UTF_8);
        ZipEntry entry = new ZipEntry("utf_ken_all.csv");
        zipOut.putNextEntry(entry);
        zipOut.write(csv);
        zipOut.finish();
        zipOut.closeEntry();

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());

        InputStream spiedIn = Mockito.spy(bytesIn);
        Mockito.doThrow(new IOException()).when(spiedIn).close();

        Resource resource = Mockito.mock(Resource.class);
        Mockito.doReturn(spiedIn).when(resource).getInputStream();

        AppProperties appProps = Mockito.mock(AppProperties.class);
        Mockito.doReturn(resource).when(appProps).getResource();

        ResourceZippedCsvFileLoader<PostCsv, PostCsv> loader = new ResourceZippedCsvFileLoader<>(appProps);

        // When
        try (CloseableIterator<PostCsv> iter = loader.load(row -> row)) {
            List<PostCsv> posts = StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                    iter, Spliterator.ORDERED), false).toList();

            Assertions.assertThat(posts.size()).isEqualTo(1);

            Assertions.assertThat(posts.get(0).localGovCode()).isEqualTo("01101");
            Assertions.assertThat(posts.get(0).postcode5()).isEqualTo("060  ");
            Assertions.assertThat(posts.get(0).postcode7()).isEqualTo("0600000");
            Assertions.assertThat(posts.get(0).prefectureName()).isEqualTo("北海道");
            Assertions.assertThat(posts.get(0).municipalityName()).isEqualTo("札幌市中央区");
            Assertions.assertThat(posts.get(0).townAreaName()).isEqualTo("以下に掲載がない場合");
            Assertions.assertThat(posts.get(0).prefectureNameKatakana()).isEqualTo("ホッカイドウ");
            Assertions.assertThat(posts.get(0).municipalityNameKatakana()).isEqualTo("サッポロシチュウオウク");
            Assertions.assertThat(posts.get(0).townAreaNameKatakana()).isEqualTo("イカニケイサイガナイバアイ");
            Assertions.assertThat(posts.get(0).townAreaWithMultiplePostcodes()).isEqualTo("0");
            Assertions.assertThat(posts.get(0).townAreaWithAddressNumbersPerKoaza()).isEqualTo("0");
            Assertions.assertThat(posts.get(0).townAreaWithChome()).isEqualTo("0");
            Assertions.assertThat(posts.get(0).postcodeWithMultipleTownAreas()).isEqualTo("0");
            Assertions.assertThat(posts.get(0).changed()).isEqualTo("0");
            Assertions.assertThat(posts.get(0).changeReason()).isEqualTo("0");
        } catch (Exception e) {
            // Then
            Assertions.assertThat(e).isInstanceOf(UncheckedIOException.class);
            Assertions.assertThat(e).hasCauseInstanceOf(IOException.class);
            return;
        }

        Assertions.fail("Should be thrown exception on resource closing.");
    }
}

package com.yo1000.postcode.infrastructure;

import com.yo1000.postcode.application.port.PostCsv;
import com.yo1000.postcode.config.AppProperties;
import com.yo1000.postcode.domain.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.data.util.CloseableIterator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

        ResourceZippedCsvFileLoader loader = new ResourceZippedCsvFileLoader(appProps);

        // When
        CloseableIterator<Post> iter = loader.load(PostCsv::toPost);

        // Then
        List<Post> posts = StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iter, Spliterator.ORDERED), false).toList();

        Assertions.assertThat(posts.size()).isEqualTo(3);
        Assertions.assertThat(posts.get(0).postcode7()).isEqualTo("0600000");
        Assertions.assertThat(posts.get(1).postcode7()).isEqualTo("1000000");
        Assertions.assertThat(posts.get(2).postcode7()).isEqualTo("2300000");
    }
}

package ru.sstu.mt.sklonyator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sstu.mt.sklonyator.enums.RussianGrammems;
import ru.sstu.mt.sklonyator.enums.RussianPoS;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SklonyatorApiImpl implements SklonyatorApi {
    private final String API_KEY = "&api_key=38d6bd9795f4573b3a8556e0f9598b8a";
    private final String POS_URL = "https://htmlweb.ru/service/api.php?partofspeech=%s&json"+API_KEY;
    private final String GRAMMEMS_URL = "https://htmlweb.ru/service/api.php?grammems=%s&json"+API_KEY;
    private final String FORMAT_URL = "https://htmlweb.ru/service/api.php?inflect=%s&grammems=%s&json"+API_KEY;
    private final String LIMIT = "limit";

    private int limit = -1;

    @Override
    public RussianPoS getPos(String word) throws IOException {
        JsonNode node = request(String.format(POS_URL, word));

        String posStr = node.get("0").asText();
        setLimit(node);

        return RussianPoS.getBySystemName(posStr);
    }

    @Override
    public List<RussianGrammems> getGrammems(String word) throws IOException {
        JsonNode node = request(String.format(GRAMMEMS_URL, word));

        List<RussianGrammems> grammems = new ArrayList<>();
        for (int i = 0; ; i++) {
            JsonNode n = node.get(Integer.toString(i));
            if(n==null) break;
            String gramStr = n.asText();
            grammems.add(RussianGrammems.getBySystemName(gramStr));
        }
        setLimit(node);
        return grammems;
    }

    @Override
    public List<String> transform(String word, List<RussianGrammems> grammems) throws IOException {
        String grammemsStr = grammems.stream()
                .map(RussianGrammems::getSystemName)
                .collect(Collectors.joining(","));
        JsonNode node = request(String.format(FORMAT_URL, word, grammemsStr));

        List<String> transforms = new ArrayList<>();
        for (int i = 0; ; i++) {
            JsonNode n = node.get(Integer.toString(i));
            if(n==null) break;
            transforms.add(n.asText());
        }
        setLimit(node);
        return transforms;

    }

    private JsonNode request(String url) throws IOException {
        String response;

        try (Scanner scanner = new Scanner(new URL(url).openStream(),
                StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            response = scanner.hasNext() ? scanner.next() : "";
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response);
    }

    @Override
    public int getLimit() {
        return limit;
    }

    private void setLimit(JsonNode response) {
        this.limit = response.get(LIMIT).asInt();
    }
}

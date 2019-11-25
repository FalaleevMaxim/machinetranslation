package ru.sstu.mt.sklonyator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sstu.mt.sklonyator.enums.RussianGrammems;
import ru.sstu.mt.sklonyator.enums.RussianPos;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SklonyatorApiImpl implements SklonyatorApi {
    private static final String DEFAULT_API_KEY = "38d6bd9795f4573b3a8556e0f9598b8a";
    private static final String POS_BASE_URL = "https://htmlweb.ru/service/api.php?partofspeech=%s&json";
    private static final String GRAMMEMS_BASE_URL = "https://htmlweb.ru/service/api.php?grammems=%s&json";
    private static final String FORMAT_BASE_URL = "https://htmlweb.ru/service/api.php?inflect=%s&grammems=%s&json";
    private static final String FORMAT_WITH_POS_BASE_URL = "https://htmlweb.ru/service/api.php?inflect=%s&grammems=%s&pos=%s&json";
    private static final String LIMIT = "limit";

    private final String apiKey;
    private final String posUrl;
    private final String grammemsUrl;
    private final String formatUrl;
    private final String formatWithPosUrl;

    private int limit = -1;

    public SklonyatorApiImpl(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            this.apiKey = "";
        } else {
            this.apiKey = "&api_key=" + apiKey;
        }
        posUrl = POS_BASE_URL + this.apiKey;
        grammemsUrl = GRAMMEMS_BASE_URL + this.apiKey;
        formatUrl = FORMAT_BASE_URL + this.apiKey;
        formatWithPosUrl = FORMAT_WITH_POS_BASE_URL + this.apiKey;
    }

    public SklonyatorApiImpl() {
        this(DEFAULT_API_KEY);
    }

    @Override
    public List<RussianPos> getPos(String word) throws IOException {
        return requestList(String.format(posUrl, word), RussianPos::getBySystemName);
    }

    @Override
    public List<RussianGrammems> getGrammems(String word) throws IOException {
        return requestList(String.format(grammemsUrl, word), RussianGrammems::getBySystemName);
    }

    @Override
    public List<String> transform(String word, Collection<RussianGrammems> grammems) throws IOException {
        String grammemsStr = grammems.stream()
                .map(RussianGrammems::getSystemName)
                .collect(Collectors.joining(","));
        return requestList(String.format(formatUrl, word, grammemsStr), x -> x);
    }

    @Override
    public List<String> transform(String word, List<RussianGrammems> grammems, RussianPos pos) throws IOException {
        String grammemsStr = grammems.stream()
                .map(RussianGrammems::getSystemName)
                .collect(Collectors.joining(","));
        return requestList(String.format(formatWithPosUrl, word, grammemsStr, pos.systemName), x -> x);
    }

    private <T> List<T> requestList(String url, Function<String, T> mapping) throws IOException {
        JsonNode node = request(url);

        List<T> results = new ArrayList<>();
        for (int i = 0; ; i++) {
            JsonNode n = node.get(Integer.toString(i));
            if (n == null) break;
            results.add(mapping.apply(n.asText()));
        }
        setLimit(node);
        return results;
    }

    private JsonNode request(String url) throws IOException {
        String response;

        try (Scanner scanner = new Scanner(new URL(url).openStream(),
                StandardCharsets.UTF_8.toString())) {
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

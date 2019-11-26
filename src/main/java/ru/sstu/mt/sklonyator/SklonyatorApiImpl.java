package ru.sstu.mt.sklonyator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sstu.mt.pipeline.PipelineLogger;
import ru.sstu.mt.sklonyator.enums.RussianGrammem;
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

import static ru.sstu.mt.pipeline.PipelineEvent.SKLONYATOR;

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

    private PipelineLogger logger = PipelineLogger.getLogger();

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
        List<RussianPos> posList = requestList(String.format(posUrl, word), RussianPos::getBySystemName);
        if (posList.isEmpty()) {
            logger.logResult(() -> String.format("No parts of speech found for word \"%s\"", word), SKLONYATOR);
        } else if (posList.size() > 1) {
            logger.logResult(() -> String.format("Multiple parts of speech found for word \"%s\":\n%s", word, posList), SKLONYATOR);
        }
        return posList;
    }

    @Override
    public List<RussianGrammem> getGrammems(String word) throws IOException {
        List<RussianGrammem> grammems = requestList(String.format(grammemsUrl, word), RussianGrammem::getBySystemName);
        if (grammems.isEmpty()) {
            logger.logResult(String.format("No grammems found for word %s", word), SKLONYATOR);
        }
        return grammems;
    }

    @Override
    public List<String> transform(String word, Collection<RussianGrammem> grammems) throws IOException {
        String grammemsStr = grammems.stream()
                .map(RussianGrammem::getSystemName)
                .collect(Collectors.joining(","));
        List<String> transforms = requestList(String.format(formatUrl, word, grammemsStr), x -> x);
        if (transforms.isEmpty()) {
            logger.logResult(() -> String.format("No options found for word \"%s\" and grammems %s", word, grammems), SKLONYATOR);
        } else if (transforms.size() > 1) {
            logger.logResult(() -> String.format("Multiple options found for word \"%s\" and grammems %s:\n%s", word, grammems, transforms), SKLONYATOR);
        }
        return transforms;
    }

    @Override
    public List<String> transform(String word, List<RussianGrammem> grammems, RussianPos pos) throws IOException {
        String grammemsStr = grammems.stream()
                .map(RussianGrammem::getSystemName)
                .collect(Collectors.joining(","));
        List<String> transforms = requestList(String.format(formatWithPosUrl, word, grammemsStr, pos.systemName), x -> x);
        if (transforms.isEmpty()) {
            logger.logResult(() -> String.format("No options found for word \"%s\", part of speech %s and grammems %s", word, pos.description, grammems), SKLONYATOR);
        } else if (transforms.size() > 1) {
            logger.logResult(() -> String.format("Multiple options found for word \"%s\", part of speech %s and grammems %s:\n%s", word, pos.description, grammems, transforms), SKLONYATOR);
        }
        return transforms;
    }

    private <T> List<T> requestList(String url, Function<String, T> mapping) throws IOException {
        JsonNode node = request(url);

        List<T> results = new ArrayList<>();
        for (int i = 0; ; i++) {
            JsonNode n = node.get(Integer.toString(i));
            if (n == null) break;
            T mapped = mapping.apply(n.asText());
            if (mapped != null) {
                results.add(mapped);
            }
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

    public SklonyatorApiImpl setLogger(PipelineLogger logger) {
        this.logger = logger;
        return this;
    }
}

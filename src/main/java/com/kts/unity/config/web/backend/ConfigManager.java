package com.kts.unity.config.web.backend;

import com.kts.unity.config.web.actions.ViewRemoteLogsPage;
import com.kts.unity.config.web.backend.dao.GameNavigationStatisticsDAO;
import com.kts.unity.config.web.backend.dao.GameUsageStatisticsDAO;
import com.kts.unity.config.web.backend.dao.LoggingRecordsDAO;
import com.kts.unity.config.web.backend.utils.AchievementTags;
import com.kts.unity.config.web.backend.utils.AvatarTags;
import com.kts.unity.config.web.backend.utils.FileUtils;
import com.kts.unity.config.web.backend.utils.PerksTags;
import com.kts.unity.config.web.backend.utils.RocketsTags;
import com.kts.unity.config.web.entities.configs.ConfigEntry;
import com.kts.unity.config.web.entities.configs.Configuration;
import com.kts.unity.config.web.backend.utils.AppConfigurationTags;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.backend.utils.WeaponTags;
import com.kts.unity.config.web.backend.utils.email.EmailType;
import com.kts.unity.config.web.backend.utils.email.EmailUtil;
import com.kts.unity.config.web.entities.GameNavigationStatistics;
import com.kts.unity.config.web.entities.configs.Achievement;
import com.kts.unity.config.web.entities.configs.AchievementsConfiguration;
import com.kts.unity.config.web.entities.configs.AvatarEntry;
import com.kts.unity.config.web.entities.configs.AvatarsConfiguration;
import com.kts.unity.config.web.entities.GameUsageStatistics;
import com.kts.unity.config.web.entities.LoggingRecord;
import com.kts.unity.config.web.entities.configs.BotsConfiguration;
import com.kts.unity.config.web.entities.configs.PerkEntry;
import com.kts.unity.config.web.entities.configs.PerksConfiguration;
import com.kts.unity.config.web.entities.configs.RocketConfigEntry;
import com.kts.unity.config.web.entities.configs.RocketsConfiguration;
import com.kts.unity.config.web.entities.configs.WeaponConfiguration;
import com.kts.unity.config.web.entities.configs.WeaponEntry;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.Dictionary;
import com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl;
import java.io.BufferedReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

public class ConfigManager {

    private ClassPathResource mobileAppConfigFileRes;
    private ClassPathResource rocketsConfigFileRes;
    private ClassPathResource perksConfigFileRes;
    private ClassPathResource weaponConfigFileRes;
    private ClassPathResource avatarsConfigFileRes;
    private ClassPathResource achievmentsConfigFileRes;
    private ClassPathResource botsConfigFileRes;
    private GameUsageStatisticsDAO gameUsageStatisticsDAO;
    private GameNavigationStatisticsDAO gameNavigationStatisticsDAO;
    private LoggingRecordsDAO loggingRecordsDAO;
    
    public static final String LOGS_HELATH_MONITORING_TIMESTAMP_MILLIS_KEY = "logsMonitoringTimestamp";
    public static final String LOGS_HEALTH_MONITORING_NUM_OF_LOGS_IN_QUEUE_KEY = "numOfLogsInQueue";
    

    public int updateConfigWithFile(File file, String configFilePath, int updateConfigInSystem) {
        int status = 1;

        try {
            InputStream is = new FileInputStream(file);

            Document doc = null;
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            doc = builder.parse(is);

            is.close();

            XPathFactory factory = new XPathFactoryImpl();
            XPathExpression expr = null;
            expr = factory.newXPath().compile("//" + AppConfigurationTags.ROOT_TAG + "/" + AppConfigurationTags.DESCRIPTION);
            Configuration.getInstance().setDescription((String) expr.evaluate(doc, XPathConstants.STRING));

            //Read config params
            expr = factory.newXPath().compile("//" + AppConfigurationTags.ROOT_TAG + "/" + AppConfigurationTags.CONFIG_PARAM);
            NodeList nodeListConfigParams = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            ArrayList<ConfigEntry> confParams = new ArrayList<ConfigEntry>();
            for (int k = 0; k < nodeListConfigParams.getLength(); k++) {
                expr = factory.newXPath().compile(AppConfigurationTags.NAME);
                String confName = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                confName = confName.replaceAll("\n", "").trim();
                expr = factory.newXPath().compile(AppConfigurationTags.VALUE);
                String confVal = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                confVal = confVal.replaceAll("\n", "").trim();
                expr = factory.newXPath().compile(AppConfigurationTags.DISPLAY_NAME);
                String confDisplName = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                confDisplName = confDisplName.replaceAll("\n", "").trim();
                ConfigEntry entry = new ConfigEntry(confName, confVal, confDisplName);
                confParams.add(entry);
            }


            //Save file in system if according parameter set to 1 this is neccessary not to overwrite file in case of initialization
            if (updateConfigInSystem == 1) {
                boolean saved = FileUtils.saveConfigInFile(file, configFilePath);
                if (!saved) {
                    throw new IOException();
                }
            }

            Configuration.getInstance().setEntries(confParams);
            Configuration.getInstance().updateXmlOutput();
            Configuration.getInstance().setInitialized(true);


        } catch (FileNotFoundException ex) {
            status = 3;
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            status = 4;
            ex.printStackTrace();
        } catch (SAXException ex) {
            status = 5;
            ex.printStackTrace();
        } catch (IOException ex) {
            status = 6;
            ex.printStackTrace();
        } catch (XPathExpressionException ex) {
            status = 7;
            ex.printStackTrace();
        } catch (Exception ex) {
            status = 0;
            ex.printStackTrace();
        }

        return status;
    }

    public void init() {
        try {
            this.updateConfigWithFile(mobileAppConfigFileRes.getFile(), null, 0);
            this.updateRocketsConfigsWithFile(rocketsConfigFileRes.getFile(), null, 0);
            this.updatePerksConfigsWithFile(perksConfigFileRes.getFile(), null, 0);
            this.updateWeaponConfigsWithFile(weaponConfigFileRes.getFile(), null, 0);
            this.updateAvatarConfigsWithFile(avatarsConfigFileRes.getFile(), null, 0);
            this.updateBotsConfigsWithFile(botsConfigFileRes.getFile(), null, 0);
            this.updateAchievementsConfigsWithFile(achievmentsConfigFileRes.getFile(), null, 0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int updateConfigFile(Configuration configuration, String pathToFileInSystem) {
        int status = 0;
        StringBuilder outputInFile = new StringBuilder();
        outputInFile.append("<" + AppConfigurationTags.ROOT_TAG + ">");
        outputInFile.append("\n");
        outputInFile.append("<" + AppConfigurationTags.DESCRIPTION + ">");
        outputInFile.append(configuration.getDescription());
        outputInFile.append("</" + AppConfigurationTags.DESCRIPTION + ">");
        outputInFile.append("\n");

        for (int k = 0; k < configuration.getEntries().size(); k++) {
            outputInFile.append("<" + AppConfigurationTags.CONFIG_PARAM + ">");

            outputInFile.append("\n");
            outputInFile.append("<" + AppConfigurationTags.NAME + ">");
            outputInFile.append(configuration.getEntries().get(k).getName());
            outputInFile.append("</" + AppConfigurationTags.NAME + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + AppConfigurationTags.VALUE + ">");
            outputInFile.append(configuration.getEntries().get(k).getValue());
            outputInFile.append("</" + AppConfigurationTags.VALUE + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + AppConfigurationTags.DISPLAY_NAME + ">");
            outputInFile.append(configuration.getEntries().get(k).getDisplayName());
            outputInFile.append("</" + AppConfigurationTags.DISPLAY_NAME + ">");
            outputInFile.append("\n");

            outputInFile.append("</" + AppConfigurationTags.CONFIG_PARAM + ">");
            outputInFile.append("\n");
        }

        outputInFile.append("</" + AppConfigurationTags.ROOT_TAG + ">");


        if (FileUtils.saveXMLStringToFile(outputInFile.toString(), pathToFileInSystem)) {
            status = 1;
        } else {
            status = 3;
        }

        return status;
    }

    public int updateAchievementsConfigsWithFile(File uploadedFile, String configFilePath, int updateConfigInSystem) {
        int status = 1;

        try {
            InputStream is = new FileInputStream(uploadedFile);

            Document doc = null;
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            doc = builder.parse(is);

            is.close();

            XPathFactory factory = new XPathFactoryImpl();
            XPathExpression expr = null;
            expr = factory.newXPath().compile("//" + AchievementTags.ROOT_TAG + "/" + AchievementTags.DESCRIPTION);
            AchievementsConfiguration.getInstance().setDescription((String) expr.evaluate(doc, XPathConstants.STRING));

            //Read config params
            expr = factory.newXPath().compile("//" + AchievementTags.ROOT_TAG + "/" + AchievementTags.ENTRY);
            NodeList nodeListConfigParams = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            ArrayList<Achievement> confParams = new ArrayList<Achievement>();
            for (int k = 0; k < nodeListConfigParams.getLength(); k++) {
                Achievement achievementConfEntry = new Achievement();

                expr = factory.newXPath().compile(AchievementTags.ID);
                String achId = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                achId = achId.replaceAll("\n", "").trim();
                achievementConfEntry.setId(achId);

                expr = factory.newXPath().compile(AchievementTags.NAME);
                String achName = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                achName = achName.replaceAll("\n", "").trim();
                achievementConfEntry.setName(achName);

                expr = factory.newXPath().compile(AchievementTags.ACH_DESCRIPTION);
                String achDescr = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                achDescr = achDescr.replaceAll("\n", "").trim();
                achievementConfEntry.setDescription(achDescr);

                expr = factory.newXPath().compile(AchievementTags.ICON);
                String achIcon = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                achIcon = achIcon.replaceAll("\n", "").trim();
                achievementConfEntry.setIcon(achIcon);

                expr = factory.newXPath().compile(AchievementTags.POINTS);
                String achPoints = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                achPoints = achPoints.replaceAll("\n", "").trim();
                achievementConfEntry.setPoints(Integer.parseInt(achPoints));

                expr = factory.newXPath().compile(AchievementTags.LIST_ORDER);
                String achListOrder = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                achListOrder = achListOrder.replaceAll("\n", "").trim();
                achievementConfEntry.setListOrder(Integer.parseInt(achListOrder));

                expr = factory.newXPath().compile(AchievementTags.AWARDING_COINS);
                String achAwdCoins = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                achAwdCoins = achAwdCoins.replaceAll("\n", "").trim();
                achievementConfEntry.setAwardingCoins(Integer.parseInt(achAwdCoins));

                expr = factory.newXPath().compile(AchievementTags.STATE);
                String achState = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                achState = achState.replaceAll("\n", "").trim();
                achievementConfEntry.setState(achState);




                confParams.add(achievementConfEntry);
            }


            if (updateConfigInSystem == 1) {
                //Save file in system
                boolean saved = FileUtils.saveConfigInFile(uploadedFile, configFilePath);
                if (!saved) {
                    throw new IOException();
                }
            }

            AchievementsConfiguration.getInstance().setEntries(confParams);
            AchievementsConfiguration.getInstance().updateXmlOutput();
            AchievementsConfiguration.getInstance().setInitialized(true);


        } catch (FileNotFoundException ex) {
            status = 3;
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            status = 4;
            ex.printStackTrace();
        } catch (SAXException ex) {
            status = 5;
            ex.printStackTrace();
        } catch (IOException ex) {
            status = 6;
            ex.printStackTrace();
        } catch (XPathExpressionException ex) {
            status = 7;
            ex.printStackTrace();
        } catch (Exception ex) {
            status = 0;
            ex.printStackTrace();
        }

        return status;
    }

    public int updateAchievementsConfigFile(AchievementsConfiguration configuration, String pathToFileInSystem) {
        int status = 0;
        StringBuilder outputInFile = new StringBuilder();
        outputInFile.append("<" + AchievementTags.ROOT_TAG + ">");
        outputInFile.append("\n");
        outputInFile.append("<" + AchievementTags.DESCRIPTION + ">");
        outputInFile.append(configuration.getDescription());
        outputInFile.append("</" + AchievementTags.DESCRIPTION + ">");
        outputInFile.append("\n");

        for (int k = 0; k < configuration.getEntries().size(); k++) {
            outputInFile.append("<" + AchievementTags.ENTRY + ">");


            outputInFile.append("\n");
            outputInFile.append("<" + AchievementTags.ID + ">");
            outputInFile.append(configuration.getEntries().get(k).getId());
            outputInFile.append("</" + AchievementTags.ID + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + AchievementTags.NAME + ">");
            outputInFile.append(configuration.getEntries().get(k).getName());
            outputInFile.append("</" + AchievementTags.NAME + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + AchievementTags.ACH_DESCRIPTION + ">");
            outputInFile.append(configuration.getEntries().get(k).getDescription());
            outputInFile.append("</" + AchievementTags.ACH_DESCRIPTION + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + AchievementTags.ICON + ">");
            outputInFile.append(configuration.getEntries().get(k).getIcon());
            outputInFile.append("</" + AchievementTags.ICON + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + AchievementTags.POINTS + ">");
            outputInFile.append(configuration.getEntries().get(k).getPoints());
            outputInFile.append("</" + AchievementTags.POINTS + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + AchievementTags.LIST_ORDER + ">");
            outputInFile.append(configuration.getEntries().get(k).getListOrder());
            outputInFile.append("</" + AchievementTags.LIST_ORDER + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + AchievementTags.AWARDING_COINS + ">");
            outputInFile.append(configuration.getEntries().get(k).getAwardingCoins());
            outputInFile.append("</" + AchievementTags.AWARDING_COINS + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + AchievementTags.STATE + ">");
            outputInFile.append(configuration.getEntries().get(k).getState());
            outputInFile.append("</" + AchievementTags.STATE + ">");
            outputInFile.append("\n");


            outputInFile.append("</" + AchievementTags.ENTRY + ">");
            outputInFile.append("\n");
        }

        outputInFile.append("</" + AchievementTags.ROOT_TAG + ">");


        if (FileUtils.saveXMLStringToFile(outputInFile.toString(), pathToFileInSystem)) {
            status = 1;
        } else {
            status = 3;
        }

        return status;
    }

    public int updateRocketsConfigsWithFile(File uploadedFile, String configFilePath, int updateConfigInSystem) {
        int status = 1;

        try {
            InputStream is = new FileInputStream(uploadedFile);

            Document doc = null;
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            doc = builder.parse(is);

            is.close();

            XPathFactory factory = new XPathFactoryImpl();
            XPathExpression expr = null;
            expr = factory.newXPath().compile("//" + RocketsTags.ROOT_TAG + "/" + RocketsTags.DESCRIPTION);
            RocketsConfiguration.getInstance().setDescription((String) expr.evaluate(doc, XPathConstants.STRING));

            //Read config params
            expr = factory.newXPath().compile("//" + RocketsTags.ROOT_TAG + "/" + RocketsTags.ROCKET_ENTRY);
            NodeList nodeListConfigParams = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            ArrayList<RocketConfigEntry> confParams = new ArrayList<RocketConfigEntry>();
            for (int k = 0; k < nodeListConfigParams.getLength(); k++) {
                RocketConfigEntry rocketConfEntry = new RocketConfigEntry();

                expr = factory.newXPath().compile(RocketsTags.ID);
                String rockId = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                rockId = rockId.replaceAll("\n", "").trim();
                rocketConfEntry.setId(rockId);

                expr = factory.newXPath().compile(RocketsTags.NAME);
                String rockName = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                rockName = rockName.replaceAll("\n", "").trim();
                rocketConfEntry.setName(rockName);

                expr = factory.newXPath().compile(RocketsTags.SHIP_TYPE);
                String shipType = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                shipType = shipType.replaceAll("\n", "").trim();
                rocketConfEntry.setShipType(shipType);

                expr = factory.newXPath().compile(RocketsTags.THRUST);
                String thrust = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                thrust = thrust.replaceAll("\n", "").trim();
                rocketConfEntry.setThrust(thrust);

                expr = factory.newXPath().compile(RocketsTags.ROTATION_SPEED);
                String rotationSpeed = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                rotationSpeed = rotationSpeed.replaceAll("\n", "").trim();
                rocketConfEntry.setRotationSpeed(rotationSpeed);


                expr = factory.newXPath().compile(RocketsTags.MASS);
                String mass = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                mass = mass.replaceAll("\n", "").trim();
                rocketConfEntry.setMass(mass);

                expr = factory.newXPath().compile(RocketsTags.DRAG);
                String drag = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                drag = drag.replaceAll("\n", "").trim();
                rocketConfEntry.setDrag(drag);

                expr = factory.newXPath().compile(RocketsTags.ANGULAR_DRAG);
                String angularDrag = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                angularDrag = angularDrag.replaceAll("\n", "").trim();
                rocketConfEntry.setAngularDrag(angularDrag);

                expr = factory.newXPath().compile(RocketsTags.CAGE_LIMIT);
                String cageLimit = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                cageLimit = cageLimit.replaceAll("\n", "").trim();
                rocketConfEntry.setCageLimit(cageLimit);

                expr = factory.newXPath().compile(RocketsTags.HIT_POINTS);
                String hitPoints = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                hitPoints = hitPoints.replaceAll("\n", "").trim();
                rocketConfEntry.setHitPoints(hitPoints);

                expr = factory.newXPath().compile(RocketsTags.RELOAD_TIME);
                String reloadTime = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                reloadTime = reloadTime.replaceAll("\n", "").trim();
                rocketConfEntry.setReloadTime(reloadTime);

                expr = factory.newXPath().compile(RocketsTags.BEAM_VELOCITY);
                String beamVelocity = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                beamVelocity = beamVelocity.replaceAll("\n", "").trim();
                rocketConfEntry.setBeamVelocity(beamVelocity);

                expr = factory.newXPath().compile(RocketsTags.FUSE_TIMER);
                String fuseTimer = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                fuseTimer = fuseTimer.replaceAll("\n", "").trim();
                rocketConfEntry.setFuseTimer(fuseTimer);

                expr = factory.newXPath().compile(RocketsTags.RANK);
                String rank = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                rank = rank.replaceAll("\n", "").trim();
                rocketConfEntry.setRank(rank);

                expr = factory.newXPath().compile(RocketsTags.PRICE_COINS);
                String priceCoins = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                priceCoins = priceCoins.replaceAll("\n", "").trim();
                rocketConfEntry.setPriceCoins(priceCoins);


                confParams.add(rocketConfEntry);
            }


            if (updateConfigInSystem == 1) {
                //Save file in system
                boolean saved = FileUtils.saveConfigInFile(uploadedFile, configFilePath);
                if (!saved) {
                    throw new IOException();
                }
            }

            RocketsConfiguration.getInstance().setEntries(confParams);
            RocketsConfiguration.getInstance().updateXmlOutput();
            RocketsConfiguration.getInstance().setInitialized(true);


        } catch (FileNotFoundException ex) {
            status = 3;
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            status = 4;
            ex.printStackTrace();
        } catch (SAXException ex) {
            status = 5;
            ex.printStackTrace();
        } catch (IOException ex) {
            status = 6;
            ex.printStackTrace();
        } catch (XPathExpressionException ex) {
            status = 7;
            ex.printStackTrace();
        } catch (Exception ex) {
            status = 0;
            ex.printStackTrace();
        }

        return status;
    }

    public int updateRocketsConfigFile(RocketsConfiguration configuration, String pathToFileInSystem) {
        int status = 0;
        StringBuilder outputInFile = new StringBuilder();
        outputInFile.append("<" + RocketsTags.ROOT_TAG + ">");
        outputInFile.append("\n");
        outputInFile.append("<" + RocketsTags.DESCRIPTION + ">");
        outputInFile.append(configuration.getDescription());
        outputInFile.append("</" + RocketsTags.DESCRIPTION + ">");
        outputInFile.append("\n");

        for (int k = 0; k < configuration.getEntries().size(); k++) {
            outputInFile.append("<" + RocketsTags.ROCKET_ENTRY + ">");


            outputInFile.append("\n");
            outputInFile.append("<" + RocketsTags.ID + ">");
            outputInFile.append(configuration.getEntries().get(k).getId());
            outputInFile.append("</" + RocketsTags.ID + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.NAME + ">");
            outputInFile.append(configuration.getEntries().get(k).getName());
            outputInFile.append("</" + RocketsTags.NAME + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.SHIP_TYPE + ">");
            outputInFile.append(configuration.getEntries().get(k).getShipType());
            outputInFile.append("</" + RocketsTags.SHIP_TYPE + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.THRUST + ">");
            outputInFile.append(configuration.getEntries().get(k).getThrust());
            outputInFile.append("</" + RocketsTags.THRUST + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.ROTATION_SPEED + ">");
            outputInFile.append(configuration.getEntries().get(k).getRotationSpeed());
            outputInFile.append("</" + RocketsTags.ROTATION_SPEED + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.MASS + ">");
            outputInFile.append(configuration.getEntries().get(k).getMass());
            outputInFile.append("</" + RocketsTags.MASS + ">");
            outputInFile.append("\n");


            outputInFile.append("<" + RocketsTags.DRAG + ">");
            outputInFile.append(configuration.getEntries().get(k).getDrag());
            outputInFile.append("</" + RocketsTags.DRAG + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.ANGULAR_DRAG + ">");
            outputInFile.append(configuration.getEntries().get(k).getAngularDrag());
            outputInFile.append("</" + RocketsTags.ANGULAR_DRAG + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.CAGE_LIMIT + ">");
            outputInFile.append(configuration.getEntries().get(k).getCageLimit());
            outputInFile.append("</" + RocketsTags.CAGE_LIMIT + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.HIT_POINTS + ">");
            outputInFile.append(configuration.getEntries().get(k).getHitPoints());
            outputInFile.append("</" + RocketsTags.HIT_POINTS + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.RELOAD_TIME + ">");
            outputInFile.append(configuration.getEntries().get(k).getReloadTime());
            outputInFile.append("</" + RocketsTags.RELOAD_TIME + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.BEAM_VELOCITY + ">");
            outputInFile.append(configuration.getEntries().get(k).getBeamVelocity());
            outputInFile.append("</" + RocketsTags.BEAM_VELOCITY + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.FUSE_TIMER + ">");
            outputInFile.append(configuration.getEntries().get(k).getFuseTimer());
            outputInFile.append("</" + RocketsTags.FUSE_TIMER + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.RANK + ">");
            outputInFile.append(configuration.getEntries().get(k).getRank());
            outputInFile.append("</" + RocketsTags.RANK + ">");
            outputInFile.append("\n");

            outputInFile.append("<" + RocketsTags.PRICE_COINS + ">");
            outputInFile.append(configuration.getEntries().get(k).getPriceCoins());
            outputInFile.append("</" + RocketsTags.PRICE_COINS + ">");
            outputInFile.append("\n");


            outputInFile.append("</" + RocketsTags.ROCKET_ENTRY + ">");
            outputInFile.append("\n");
        }

        outputInFile.append("</" + RocketsTags.ROOT_TAG + ">");


        if (FileUtils.saveXMLStringToFile(outputInFile.toString(), pathToFileInSystem)) {
            status = 1;
        } else {
            status = 3;
        }

        return status;
    }

    public int updatePerksConfigsWithFile(File uploadedFile, String configFilePath, int updateConfigInSystem) {
        int status = 1;

        try {
            InputStream is = new FileInputStream(uploadedFile);

            Document doc = null;
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            doc = builder.parse(is);

            is.close();

            XPathFactory factory = new XPathFactoryImpl();
            XPathExpression expr = null;
            expr = factory.newXPath().compile("//" + PerksTags.ROOT_TAG + "/" + PerksTags.DESCRIPTION);
            PerksConfiguration.getInstance().setDescription((String) expr.evaluate(doc, XPathConstants.STRING));

            //Read config params
            expr = factory.newXPath().compile("//" + PerksTags.ROOT_TAG + "/" + PerksTags.PERK_ENTRY);
            NodeList nodeListConfigParams = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            ArrayList<PerkEntry> perks = new ArrayList<PerkEntry>();
            for (int k = 0; k < nodeListConfigParams.getLength(); k++) {
                PerkEntry perkEntry = new PerkEntry();

                expr = factory.newXPath().compile(PerksTags.ID);
                String perkId = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                perkId = perkId.replaceAll("\n", "").trim();
                perkEntry.setId(perkId);

                expr = factory.newXPath().compile(PerksTags.NAME);
                String perkName = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                perkName = perkName.replaceAll("\n", "").trim();
                perkEntry.setName(perkName);

                expr = factory.newXPath().compile(PerksTags.DESCRIPTION);
                String perkDescr = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                perkDescr = perkDescr.replaceAll("\n", "").trim();
                perkEntry.setDescription(perkDescr);

                expr = factory.newXPath().compile(PerksTags.MIN_RANK);
                String minRank = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                minRank = minRank.replaceAll("\n", "").trim();
                perkEntry.setMinrank(minRank);

                expr = factory.newXPath().compile(PerksTags.PRICE_COINS);
                String price = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                price = price.replaceAll("\n", "").trim();
                perkEntry.setPriceCoins(price);


                perks.add(perkEntry);
            }


            if (updateConfigInSystem == 1) {
                //Save file in system
                boolean saved = FileUtils.saveConfigInFile(uploadedFile, configFilePath);
                if (!saved) {
                    throw new IOException();
                }
            }

            PerksConfiguration.getInstance().setEntries(perks);
            PerksConfiguration.getInstance().updateXmlOutput();
            PerksConfiguration.getInstance().setInitialized(true);


        } catch (FileNotFoundException ex) {
            status = 3;
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            status = 4;
            ex.printStackTrace();
        } catch (SAXException ex) {
            status = 5;
            ex.printStackTrace();
        } catch (IOException ex) {
            status = 6;
            ex.printStackTrace();
        } catch (XPathExpressionException ex) {
            status = 7;
            ex.printStackTrace();
        } catch (Exception ex) {
            status = 0;
            ex.printStackTrace();
        }

        return status;
    }

    public int updateBotsConfigsWithFile(File uploadedFile, String configFilePath, int updateConfigInSystem) {
        int status = 1;
        try {
            InputStream is = new FileInputStream(uploadedFile);

            BufferedReader br = null;
            StringBuilder fileContent = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    fileContent.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            BotsConfiguration.getInstance().setXmlOutput(fileContent.toString());

            if (updateConfigInSystem == 1) {
                //Save file in system
                boolean saved = FileUtils.saveConfigInFile(uploadedFile, configFilePath);
                if (!saved) {
                    throw new IOException();
                }
            }

            status = 1;


        } catch (Exception ex) {
            status = 3;
            ex.printStackTrace();
        }

        return status;
    }

    public int updateAvatarConfigsWithFile(File uploadedFile, String configFilePath, int updateConfigInSystem) {
        int status = 1;

        try {
            InputStream is = new FileInputStream(uploadedFile);

            Document doc = null;
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            doc = builder.parse(is);

            is.close();

            XPathFactory factory = new XPathFactoryImpl();
            XPathExpression expr = null;
            expr = factory.newXPath().compile("//" + AvatarTags.ROOT_TAG + "/" + WeaponTags.DESCRIPTION);
            AvatarsConfiguration.getInstance().setDescription((String) expr.evaluate(doc, XPathConstants.STRING));

            //Read config params
            expr = factory.newXPath().compile("//" + AvatarTags.ROOT_TAG + "/" + AvatarTags.ENTRY);
            NodeList nodeListConfigParams = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            ArrayList<AvatarEntry> avatars = new ArrayList<AvatarEntry>();
            for (int k = 0; k < nodeListConfigParams.getLength(); k++) {
                AvatarEntry avaEntry = new AvatarEntry();

                expr = factory.newXPath().compile(AvatarTags.ID);
                String avaId = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                avaId = avaId.replaceAll("\n", "").trim();
                avaEntry.setId(avaId);

                expr = factory.newXPath().compile(AvatarTags.NAME);
                String avaName = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                avaName = avaName.replaceAll("\n", "").trim();
                avaEntry.setName(avaName);

                expr = factory.newXPath().compile(AvatarTags.URL);
                String avaImgUrl = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                avaImgUrl = avaImgUrl.replaceAll("\n", "").trim();
                avaEntry.setUrl(avaImgUrl);

                expr = factory.newXPath().compile(AvatarTags.EXPERIENCE_POINTS);
                String expPoints = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                expPoints = expPoints.replaceAll("\n", "").trim();
                avaEntry.setExpoints(expPoints);


                avatars.add(avaEntry);
            }


            if (updateConfigInSystem == 1) {
                //Save file in system
                boolean saved = FileUtils.saveConfigInFile(uploadedFile, configFilePath);
                if (!saved) {
                    throw new IOException();
                }
            }

            AvatarsConfiguration.getInstance().setEntries(avatars);
            AvatarsConfiguration.getInstance().updateXmlOutput();
            AvatarsConfiguration.getInstance().setInitialized(true);
        } catch (FileNotFoundException ex) {
            status = 3;
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            status = 4;
            ex.printStackTrace();
        } catch (SAXException ex) {
            status = 5;
            ex.printStackTrace();
        } catch (IOException ex) {
            status = 6;
            ex.printStackTrace();
        } catch (XPathExpressionException ex) {
            status = 7;
            ex.printStackTrace();
        } catch (Exception ex) {
            status = 0;
            ex.printStackTrace();
        }

        return status;
    }

    public int updateWeaponConfigsWithFile(File uploadedFile, String configFilePath, int updateConfigInSystem) {
        int status = 1;

        try {
            InputStream is = new FileInputStream(uploadedFile);

            Document doc = null;
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            doc = builder.parse(is);

            is.close();

            XPathFactory factory = new XPathFactoryImpl();
            XPathExpression expr = null;
            expr = factory.newXPath().compile("//" + WeaponTags.ROOT_TAG + "/" + WeaponTags.DESCRIPTION);
            WeaponConfiguration.getInstance().setDescription((String) expr.evaluate(doc, XPathConstants.STRING));

            //Read config params
            expr = factory.newXPath().compile("//" + WeaponTags.ROOT_TAG + "/" + WeaponTags.WEAPON_ENTRY);
            NodeList nodeListConfigParams = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            ArrayList<WeaponEntry> weapon = new ArrayList<WeaponEntry>();
            for (int k = 0; k < nodeListConfigParams.getLength(); k++) {
                WeaponEntry weaponEntry = new WeaponEntry();

                expr = factory.newXPath().compile(WeaponTags.ID);
                String weaponId = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                weaponId = weaponId.replaceAll("\n", "").trim();
                weaponEntry.setId(weaponId);

                expr = factory.newXPath().compile(WeaponTags.NAME);
                String weaponName = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                weaponName = weaponName.replaceAll("\n", "").trim();
                weaponEntry.setName(weaponName);

                expr = factory.newXPath().compile(WeaponTags.DESCRIPTION);
                String weaponDescription = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                weaponDescription = weaponDescription.replaceAll("\n", "").trim();
                weaponEntry.setDescription(weaponDescription);

                expr = factory.newXPath().compile(WeaponTags.MIN_RANK);
                String minRank = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                minRank = minRank.replaceAll("\n", "").trim();
                weaponEntry.setMinrank(minRank);

                expr = factory.newXPath().compile(WeaponTags.PRICE_COINS);
                String price = (String) expr.evaluate(nodeListConfigParams.item(k), XPathConstants.STRING);
                price = price.replaceAll("\n", "").trim();
                weaponEntry.setPriceCoins(price);


                weapon.add(weaponEntry);
            }


            if (updateConfigInSystem == 1) {
                //Save file in system
                boolean saved = FileUtils.saveConfigInFile(uploadedFile, configFilePath);
                if (!saved) {
                    throw new IOException();
                }
            }

            WeaponConfiguration.getInstance().setEntries(weapon);
            WeaponConfiguration.getInstance().updateXmlOutput();
            WeaponConfiguration.getInstance().setInitialized(true);


        } catch (FileNotFoundException ex) {
            status = 3;
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            status = 4;
            ex.printStackTrace();
        } catch (SAXException ex) {
            status = 5;
            ex.printStackTrace();
        } catch (IOException ex) {
            status = 6;
            ex.printStackTrace();
        } catch (XPathExpressionException ex) {
            status = 7;
            ex.printStackTrace();
        } catch (Exception ex) {
            status = 0;
            ex.printStackTrace();
        }

        return status;
    }

    public int saveGameUsageStatisticsRecord(GameUsageStatistics statistics, int gameMode) {
        int status = 0;


        try {
            GameUsageStatistics statInDB = this.gameUsageStatisticsDAO.getGameUsageStatistics(statistics.getApplicationId());
            if (statInDB != null) {
                switch (gameMode) {
                    case 1:
                        statistics.setNumberOfSPGames(statInDB.getNumberOfSPGames());//multiplayer mode
                        statistics.setSecondsOfUsageSPMode(statInDB.getSecondsOfUsageSPMode());
                        statistics.setUsageTime(statInDB.getUsageTime() + statistics.getLastUsageTime());
                        statistics.setUsedTimes(statInDB.getUsedTimes() + 1);
                        break;
                    case 2:
                        statistics.setNumberOfSPGames(statInDB.getNumberOfSPGames() + 1);//singleplayer mode
                        statistics.setUsedTimes(statInDB.getUsedTimes());
                        statistics.setSecondsOfUsageSPMode(statInDB.getSecondsOfUsageSPMode() + statistics.getLastUsageTime());
                        statistics.setUsageTime(statInDB.getUsageTime());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                status = this.gameUsageStatisticsDAO.updateGameUsageStatRecord(statistics);
            } else {//Creating game usage statistics record
                switch (gameMode) {
                    case 1:
                        statistics.setNumberOfSPGames(0);//multiplayer mode
                        statistics.setSecondsOfUsageSPMode(0);
                        statistics.setUsageTime(statistics.getLastUsageTime());
                        statistics.setUsedTimes(1);
                        break;
                    case 2:
                        statistics.setNumberOfSPGames(1);//singleplayer mode
                        statistics.setUsedTimes(0);
                        statistics.setSecondsOfUsageSPMode(statistics.getLastUsageTime());
                        statistics.setLastUsageTime(0);
                        statistics.setUsageTime(0);
                        break;
                    default:
                        statistics = new GameUsageStatistics();
                        break;
                }

                status = this.gameUsageStatisticsDAO.createGameUsageStatRecord(statistics);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return status;
    }

    public int removeUsageStatisticsRecord(String statisticsRecordId) {
        return this.gameUsageStatisticsDAO.delteGameUsageStatRecord(statisticsRecordId);
    }

    public ArrayList<GameUsageStatistics> getUsageStatisticsRecords(int numOfRecords) {
        return this.gameUsageStatisticsDAO.getGameUsageStatisticsList(numOfRecords);
    }

    public GameUsageStatistics getUsageStatisticsRecordForPlayer(String playerName) {
        return this.gameUsageStatisticsDAO.getGameUsageStatisticsForPlayer(playerName);
    }

    public int getNumberOfStatisticsRecords() {
        return this.gameUsageStatisticsDAO.getNumberOfRecords();
    }

    public int saveGameNavigationStatisticsRecord(GameNavigationStatistics statistics) {
        int status = 0;

        try {
            GameNavigationStatistics statInDB = this.gameNavigationStatisticsDAO.getGameNavigationStatistics(statistics.getApplicationId());
            if (statInDB != null) {
                statistics.setNumOfHangarVisits(statInDB.getNumOfHangarVisits() + statistics.getNumOfHangarVisits());
                statistics.setNumOfCoinsButtonPress(statInDB.getNumOfCoinsButtonPress() + statistics.getNumOfCoinsButtonPress());
                statistics.setFbShareAcheivement(statInDB.getFbShareAcheivement() + statistics.getFbShareAcheivement());
                statistics.setTournamentsButton(statInDB.getTournamentsButton() + statistics.getTournamentsButton());
                statistics.setLevelSquare(statInDB.getLevelSquare() + statistics.getLevelSquare());
                statistics.setLevelTriangle(statInDB.getLevelTriangle() + statistics.getLevelTriangle());
                statistics.setLevelHex(statInDB.getLevelHex() + statistics.getLevelHex());
                statistics.setFbFriends(statInDB.getFbFriends() + statistics.getFbFriends());
                statistics.setMusicOffBtn(statInDB.getMusicOffBtn() + statistics.getMusicOffBtn());
                
                statistics.setCoins100(statInDB.getCoins100() + statistics.getCoins100());
                statistics.setCoins250(statInDB.getCoins250() + statistics.getCoins250());
                statistics.setCoins550(statInDB.getCoins550() + statistics.getCoins550());
                statistics.setTraining(statInDB.getTraining() + statistics.getTraining());
                statistics.setBuy(statInDB.getBuy() + statistics.getBuy());
                statistics.setAvatar(statInDB.getAvatar() + statistics.getAvatar());
                statistics.setShip(statInDB.getShip() + statistics.getShip());
                statistics.setWeapon(statInDB.getWeapon() + statistics.getWeapon());
                statistics.setPerks(statInDB.getPerks() + statistics.getPerks());
                

                status = this.gameNavigationStatisticsDAO.updateGameNavigationStatisticsRecord(statistics);
            } else {//Creating game navigation statistics record                
                status = this.gameNavigationStatisticsDAO.createGameNavigationStatisticsRecord(statistics);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return status;
    }
    public ArrayList<GameNavigationStatistics> getNavigationStatisticsRecords(int numOfRecords) {
        return this.gameNavigationStatisticsDAO.getGameNavigationStatisticsList(numOfRecords);
    }
    public int getNumberOfNavigationStatisticsRecords() {
        return this.gameNavigationStatisticsDAO.getNumberOfRecords();
    }
    public int removeNavigationStatisticsRecord(String statisticsRecordId) {
        return this.gameNavigationStatisticsDAO.delteGameNavigationStatisticsRecord(statisticsRecordId);
    }


    public int createRemoteLogRecord(LoggingRecord logRecord) {
        return this.loggingRecordsDAO.createTransactionRecord(logRecord);
    }

    public ArrayList<LoggingRecord> getRemoteLogsList(int numOfRecords) {
        return this.loggingRecordsDAO.getLogRecords(numOfRecords);
    }
    public int getNumberOfRemoteLogsRecords() {
        return this.loggingRecordsDAO.getNumberOfRecords();
    }
    
    public int removeLogsRecords(int numOfRecordsToKeep){
        return this.loggingRecordsDAO.deleteLogsRecords(numOfRecordsToKeep);
    }
    
    public void updateRemoteLogsHealthMonitor(){
        //Remote log recorded - monitor if its frequency is acceptable
        long currentTimeMillis = (new Date()).getTime();
        long timestampInSystem = -1;
        if (Dictionary.getValue(ConfigManager.LOGS_HELATH_MONITORING_TIMESTAMP_MILLIS_KEY) != null) {
            timestampInSystem = Long.parseLong(Dictionary.getValue(ConfigManager.LOGS_HELATH_MONITORING_TIMESTAMP_MILLIS_KEY));
        } else {//no timestamp in system - create it
            Dictionary.putValue(ConfigManager.LOGS_HELATH_MONITORING_TIMESTAMP_MILLIS_KEY, String.valueOf(currentTimeMillis));
            timestampInSystem = Long.parseLong(Dictionary.getValue(ConfigManager.LOGS_HELATH_MONITORING_TIMESTAMP_MILLIS_KEY));
        }

        int numOfLogsInHealthQueue = -1;
        if (Dictionary.getValue(ConfigManager.LOGS_HEALTH_MONITORING_NUM_OF_LOGS_IN_QUEUE_KEY) != null) {
            numOfLogsInHealthQueue = Integer.parseInt(Dictionary.getValue(ConfigManager.LOGS_HEALTH_MONITORING_NUM_OF_LOGS_IN_QUEUE_KEY));
            //update logs count
            numOfLogsInHealthQueue++;
            Dictionary.putValue(ConfigManager.LOGS_HEALTH_MONITORING_NUM_OF_LOGS_IN_QUEUE_KEY, String.valueOf(numOfLogsInHealthQueue));
        } else {//queue is empty - initialize and create it
            Dictionary.putValue(ConfigManager.LOGS_HEALTH_MONITORING_NUM_OF_LOGS_IN_QUEUE_KEY, String.valueOf(1));
            numOfLogsInHealthQueue = Integer.parseInt(Dictionary.getValue((ConfigManager.LOGS_HEALTH_MONITORING_NUM_OF_LOGS_IN_QUEUE_KEY)));
        }

        long dateDiffMillis = currentTimeMillis - timestampInSystem;

        long hoursPassed = (long) Math.floor(dateDiffMillis / (60 * 60 * 1000) % 24);
        if ((hoursPassed <= Settings.getLogHealthMonitorAcceptancePeriodHours()) && (numOfLogsInHealthQueue > Settings.getLogHealthMonitorAcceptanceNumOfLogsInPeriod())) {
            if((Dictionary.getValue(ViewRemoteLogsPage.NOTIFY_ADMINS_LOG_HEALTH_MONITOR_KEY) != null) &&
                    ("true".equalsIgnoreCase(Dictionary.getValue(ViewRemoteLogsPage.NOTIFY_ADMINS_LOG_HEALTH_MONITOR_KEY)))){
                EmailUtil emailUtil = (EmailUtil) AppContext.getContext().getBean(AppSpringBeans.EMAIL_UTIL_BEAN);
                try{
                    emailUtil.generateAndSendEmail(EmailType.SEND_DIRECT_PRIVATE_EMAIL_TO_ADMINS, "Details will go here", null);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
            }
            //email sent - reset health counters
            Dictionary.putValue(ConfigManager.LOGS_HELATH_MONITORING_TIMESTAMP_MILLIS_KEY, String.valueOf(currentTimeMillis));
            Dictionary.putValue(ConfigManager.LOGS_HEALTH_MONITORING_NUM_OF_LOGS_IN_QUEUE_KEY, String.valueOf(0));
        } else {//monitor period is over - reset health monitor
            if (hoursPassed > Settings.getLogHealthMonitorAcceptancePeriodHours()) {
                Dictionary.putValue(ConfigManager.LOGS_HELATH_MONITORING_TIMESTAMP_MILLIS_KEY, String.valueOf(currentTimeMillis));
                Dictionary.putValue(ConfigManager.LOGS_HEALTH_MONITORING_NUM_OF_LOGS_IN_QUEUE_KEY, String.valueOf(1));
            }
        }
    }

    public void setMobileAppConfigFileRes(ClassPathResource mobileAppConfigFileRes) {
        this.mobileAppConfigFileRes = mobileAppConfigFileRes;
    }

    public void setRocketsConfigFileRes(ClassPathResource rocketsConfigFileRes) {
        this.rocketsConfigFileRes = rocketsConfigFileRes;
    }

    public void setPerksConfigFileRes(ClassPathResource perksConfigFileRes) {
        this.perksConfigFileRes = perksConfigFileRes;
    }

    public void setWeaponConfigFileRes(ClassPathResource weaponConfigFileRes) {
        this.weaponConfigFileRes = weaponConfigFileRes;
    }

    public void setAchievmentsConfigFileRes(ClassPathResource achievmentsConfigFileRes) {
        this.achievmentsConfigFileRes = achievmentsConfigFileRes;
    }

    public void setBotsConfigFileRes(ClassPathResource botsConfigFileRes) {
        this.botsConfigFileRes = botsConfigFileRes;
    }

    public GameUsageStatisticsDAO getGameUsageStatisticsDAO() {
        return gameUsageStatisticsDAO;
    }

    public void setGameUsageStatisticsDAO(GameUsageStatisticsDAO gameUsageStatisticsDAO) {
        this.gameUsageStatisticsDAO = gameUsageStatisticsDAO;
    }

    public GameNavigationStatisticsDAO getGameNavigationStatisticsDAO() {
        return gameNavigationStatisticsDAO;
    }

    public void setGameNavigationStatisticsDAO(GameNavigationStatisticsDAO gameNavigationStatisticsDAO) {
        this.gameNavigationStatisticsDAO = gameNavigationStatisticsDAO;
    }
    

    public void setAvatarsConfigFileRes(ClassPathResource avatarsConfigFileRes) {
        this.avatarsConfigFileRes = avatarsConfigFileRes;
    }

    public LoggingRecordsDAO getLoggingRecordsDAO() {
        return loggingRecordsDAO;
    }

    public void setLoggingRecordsDAO(LoggingRecordsDAO loggingRecordsDAO) {
        this.loggingRecordsDAO = loggingRecordsDAO;
    }
}

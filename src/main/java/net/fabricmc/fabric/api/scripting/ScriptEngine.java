package net.fabricmc.fabric.api.scripting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.scripting.ScriptCallable;
import net.fabricmc.fabric.api.scripting.ScriptFunction;
import net.fabricmc.fabric.utils.player.MovementUtils;
import net.fabricmc.fabric.utils.render.Render2DEngine;
import net.fabricmc.fabric.utils.render.RenderHelper;
import net.minecraft.text.Text;

public class ScriptEngine {
    private Map<String, ScriptCallable> functions = new HashMap<String, ScriptCallable>();
    private Map<String, Object> variables = new HashMap<String, Object>();

    public ScriptEngine() {
        this.registerFunction("print", (args, engine) -> {
            if (!args.isEmpty()) {
                ClientMain.mc.inGameHud.getChatHud().addMessage(Text.of((String)args.get(0).toString()));
            }
            return null;
        });
        this.registerFunction("getHealth", (args, engine) -> Float.valueOf(ClientMain.mc.player.getHealth()));
        this.registerFunction("drawRectangle", (args, engine) -> {
            if (args.size() < 4 || args.size() > 5) {
                return this.error("drawRectangle expects 4 or 5 arguments (x, y, width, height, [color])");
            }
            float x = this.getFloatArg(args, 0).floatValue();
            float y = this.getFloatArg(args, 1).floatValue();
            float width = this.getFloatArg(args, 2).floatValue();
            float height = this.getFloatArg(args, 3).floatValue();
            int color = args.size() == 5 ? this.getIntArg(args, 4) : -16777216;
            Render2DEngine.fill(RenderHelper.getContext().getMatrices(), x, y, x + width, y + height, color);
            return null;
        });
        this.registerFunction("drawText", (args, engine) -> {
            if (args.size() < 3 || args.size() > 4) {
                return this.error("drawText expects 3 or 4 arguments (x, y, text, [color])");
            }
            float x = this.getFloatArg(args, 0).floatValue();
            float y = this.getFloatArg(args, 1).floatValue();
            String text = args.get(2).toString();
            int color = args.size() == 4 ? this.getIntArg(args, 3) : -1;
            ClientMain.fontRenderer.draw(RenderHelper.getContext().getMatrices(), text, x, y, color);
            return null;
        });
        this.registerFunction("setPitch", (args, engine) -> {
            ClientMain.mc.player.setPitch(this.getFloatArg(args, 0).floatValue());
            return null;
        });
        this.registerFunction("setYaw", (args, engine) -> {
            ClientMain.mc.player.setYaw(this.getFloatArg(args, 0).floatValue());
            return null;
        });
        this.registerFunction("strafe", (args, engine) -> {
            MovementUtils.strafe(this.getFloatArg(args, 0).floatValue());
            return null;
        });
        this.registerFunction("isMoving", (args, engine) -> MovementUtils.isMoving());
        this.registerFunction("getSpeed", (args, engine) -> Float.valueOf(MovementUtils.getSpeed()));
        this.registerFunction("forward", (args, engine) -> MovementUtils.forward(this.getFloatArg(args, 0).floatValue()));
        this.registerFunction("setMotionY", (args, engine) -> {
            MovementUtils.setMotionY(this.getFloatArg(args, 0).floatValue());
            return null;
        });
        this.registerFunction("jump", (args, engine) -> {
            ClientMain.mc.player.jump();
            return null;
        });
        this.registerFunction("getPos", (args, engine) -> new double[]{ClientMain.mc.player.getX(), ClientMain.mc.player.getY(), ClientMain.mc.player.getZ()});
        this.registerFunction("getHurttime", (args, engine) -> ClientMain.mc.player.hurtTime);
        this.registerFunction("isOnGround", (args, engine) -> ClientMain.mc.player.isOnGround());
    }

    public void registerFunction(String name, ScriptCallable callable) {
        this.functions.put(name, callable);
    }

    public void registerUserFunction(String name, String block) {
        this.functions.put(name, new ScriptFunction(name, block));
    }

    public Object callFunction(String funcName, List<Object> args) {
        if (this.functions.containsKey(funcName)) {
            return this.functions.get(funcName).call(args, this);
        }
        System.err.println("Function not found: " + funcName);
        return null;
    }

    public void executeLine(String line) {
        Object value;
        String varName;
        if ((line = line.trim()).isEmpty()) {
            return;
        }
        if (line.endsWith("++")) {
            String varName2 = line.substring(0, line.length() - 2).trim();
            if (this.variables.containsKey(varName2)) {
                Object value2 = this.variables.get(varName2);
                if (value2 instanceof Number) {
                    Number num = (Number)value2;
                    if (num instanceof Integer) {
                        this.variables.put(varName2, num.intValue() + 1);
                    } else if (num instanceof Long) {
                        this.variables.put(varName2, num.longValue() + 1L);
                    } else if (num instanceof Float) {
                        this.variables.put(varName2, Float.valueOf(num.floatValue() + 1.0f));
                    } else if (num instanceof Double) {
                        this.variables.put(varName2, num.doubleValue() + 1.0);
                    }
                } else {
                    System.out.println("Error: Cannot increment non-numeric variable '" + varName2 + "'");
                }
            } else {
                System.out.println("Error: Variable '" + varName2 + "' not found");
            }
            return;
        }
        if (line.endsWith("--") && this.variables.containsKey(varName = line.substring(0, line.length() - 2).trim()) && (value = this.variables.get(varName)) instanceof Number) {
            Number num = (Number)value;
            if (num instanceof Integer) {
                this.variables.put(varName, num.intValue() - 1);
            } else if (num instanceof Long) {
                this.variables.put(varName, num.longValue() - 1L);
            } else if (num instanceof Float) {
                this.variables.put(varName, Float.valueOf(num.floatValue() - 1.0f));
            } else if (num instanceof Double) {
                this.variables.put(varName, num.doubleValue() - 1.0);
            }
        }
        if (line.startsWith("if ")) {
            int thenIndex = line.indexOf("{");
            if (thenIndex == -1) {
                System.out.println("Error: Missing '{' in if statement");
                return;
            }
            String conditionExpr = line.substring(3, thenIndex).trim();
            boolean condition = this.evaluateCondition(conditionExpr);
            int elseIndex = line.indexOf("} else {");
            if (condition) {
                String ifBlock = elseIndex != -1 ? line.substring(thenIndex + 1, elseIndex).trim() : line.substring(thenIndex + 1, line.length() - 1).trim();
                this.executeBlock(ifBlock);
            } else if (elseIndex != -1) {
                String elseBlock = line.substring(elseIndex + 7, line.length() - 1).trim();
                this.executeBlock(elseBlock);
            }
            return;
        }
        if (line.contains("=") && !line.contains("==")) {
            String[] parts = line.split("=", 2);
            String varName3 = parts[0].trim();
            String expr = parts[1].trim();
            Object value3 = this.evaluateExpression(expr);
            this.variables.put(varName3, value3);
            return;
        }
        if (line.contains("(") && line.endsWith(")")) {
            int parenIndex = line.indexOf("(");
            String funcName = line.substring(0, parenIndex).trim();
            String argsString = line.substring(parenIndex + 1, line.length() - 1).trim();
            ArrayList<Object> args = new ArrayList<Object>();
            if (!argsString.isEmpty()) {
                String[] argParts;
                for (String arg : argParts = argsString.split(",")) {
                    args.add(this.evaluateExpression(arg.trim()));
                }
            }
            this.callFunction(funcName, args);
        }
    }

    public Object evaluateExpression(String expr) {
        if ((expr = expr.trim()).startsWith("\"") && expr.endsWith("\"")) {
            return expr.substring(1, expr.length() - 1);
        }
        if (expr.contains("(") && expr.endsWith(")")) {
            int parenIndex = expr.indexOf("(");
            String funcName = expr.substring(0, parenIndex).trim();
            String argsString = expr.substring(parenIndex + 1, expr.length() - 1).trim();
            ArrayList<Object> args = new ArrayList<Object>();
            if (!argsString.isEmpty()) {
                String[] argParts;
                for (String arg : argParts = argsString.split(",")) {
                    args.add(this.evaluateExpression(arg.trim()));
                }
            }
            return this.callFunction(funcName, args);
        }
        try {
            return Integer.parseInt(expr);
        }
        catch (NumberFormatException e) {
            if (this.variables.containsKey(expr)) {
                return this.variables.get(expr);
            }
            return expr;
        }
    }

    public boolean evaluateCondition(String condition) {
        String[] tokens = condition.split("\\s+");
        if (tokens.length == 3) {
            Object left = this.evaluateExpression(tokens[0]);
            String operator = tokens[1];
            Object right = this.evaluateExpression(tokens[2]);
            if (left instanceof Number && right instanceof Number) {
                double leftNum = ((Number)left).doubleValue();
                double rightNum = ((Number)right).doubleValue();
                return switch (operator) {
                    case "==" -> {
                        if (leftNum == rightNum) {
                            yield true;
                        }
                        yield false;
                    }
                    case "!=" -> {
                        if (leftNum != rightNum) {
                            yield true;
                        }
                        yield false;
                    }
                    case ">" -> {
                        if (leftNum > rightNum) {
                            yield true;
                        }
                        yield false;
                    }
                    case "<" -> {
                        if (leftNum < rightNum) {
                            yield true;
                        }
                        yield false;
                    }
                    case ">=" -> {
                        if (leftNum >= rightNum) {
                            yield true;
                        }
                        yield false;
                    }
                    case "<=" -> {
                        if (leftNum <= rightNum) {
                            yield true;
                        }
                        yield false;
                    }
                    default -> false;
                };
            }
        }
        return false;
    }

    private Float getFloatArg(List<Object> args, int index) {
        try {
            return Float.valueOf(((Number)args.get(index)).floatValue());
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Expected a numeric argument at index " + index);
        }
    }

    private Integer getIntArg(List<Object> args, int index) {
        try {
            return ((Number)args.get(index)).intValue();
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Expected an integer argument at index " + index);
        }
    }

    private Object error(String message) {
        System.out.println("Error: " + message);
        return null;
    }

    public void executeBlock(String block) {
        String[] lines;
        for (String line : lines = block.split("\\n")) {
            this.executeLine(line);
        }
    }
}

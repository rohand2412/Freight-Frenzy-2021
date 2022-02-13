package org.firstinspires.ftc.teamcode.TeleOps;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Control.Robot;
import org.firstinspires.ftc.teamcode.Control._TeleOp;

@TeleOp(group="TeleOp")
public class FinalTeleOp extends _TeleOp {

    @Override
    public void init() {
        Robot.setup(hardwareMap, telemetry, Robot.SetupType.TeleOp);
        Robot.setFieldSide(Robot.FieldSide.RED);
        Robot.setCranePreset(Robot.CRANE_COLLECTION_DROP);
    }

    @Override
    public void init_loop() {
        Robot.update();
        telemetry.addLine("Bucket: " + String.valueOf(Robot.getBucket().getDegree()));
        telemetry.addLine("Lift: " + String.valueOf(Robot.getCraneIMU().getRoll()));
        telemetry.update();
    }

    @Override
    public void loop() {
        Robot.update();
        telemetry.addLine("Lift: " + Robot.getCraneIMU().getRoll());
        telemetry.addLine("Pivot: " + Robot.getCraneIMU().getYaw());
        telemetry.addLine("Bucket" + Robot.getBucket().getDegree());

        if (gamepad1.a) {
            Robot.moveCraneToPreset(Robot.CRANE_TOP_LEVEL_HOLD, true);
        }
        else if (gamepad1.b) {
            Robot.getBucket().setDegree(Robot.CRANE_COLLECTION_HOLD.BUCKET_DEGREE);
            Robot.moveCraneToPreset(Robot.CRANE_COLLECTION_HOLD, false);
        }
        else if (gamepad1.x) {
            Robot.moveCraneToPreset(Robot.CRANE_SHARED_LEVEL_HOLD, true);
        }
        else if (gamepad1.y) {
            Robot.getBucket().setDegree(Robot.CRANE_COLLECTION_HOLD.BUCKET_DEGREE);
            Robot.moveCraneToPreset(Robot.CRANE_COLLECTION_HOLD, false);
        }

        if(gamepad1.left_stick_x!=0 || gamepad1.left_stick_y!=0){
            double left_stick_y = -gamepad1.left_stick_y;
            double left_stick_x = gamepad1.left_stick_x;
            double joyStickAngleCrawl = (Math.toDegrees(Math.atan2(left_stick_y, left_stick_x)) + 360) % 360;
            double posAngCrawl = Robot.getIMU().getYaw();
            double speedCrawl = Math.hypot(left_stick_x, left_stick_y)/2.5;
            Robot.getDrivetrain().runSpeedAngle(speedCrawl,-posAngCrawl + (joyStickAngleCrawl - 90),0);
        }
        else if(gamepad1.right_stick_x!=0 || gamepad1.right_stick_y!=0){
            double right_stick_y = -gamepad1.right_stick_y;
            double right_stick_x = gamepad1.right_stick_x;
            double joyStickAngle = (Math.toDegrees(Math.atan2(right_stick_y, right_stick_x)) + 360) % 360;
            double posAng = Robot.getIMU().getYaw();
            double speed = Math.hypot(right_stick_x, right_stick_y);
            Robot.getDrivetrain().runSpeedAngle(speed,-posAng + (joyStickAngle - 90),0);
        }
        else if (gamepad1.left_bumper) {
            Robot.getIntake().runSpeed(0.8);
        }
        else if (gamepad1.right_bumper) {
            Robot.getIntake().runSpeed(-0.8);
        }
        else {
            Robot.getDrivetrain().stop();
        }

        //carousel cw
        if(gamepad2.right_trigger!=0){
            Robot.getCarousel().runSpeed(0.5);
        }
        //carouselccw
        else if(gamepad2.left_trigger!=0){
            Robot.getCarousel().runSpeed(-0.5);
        }
        //pivot ccw
        if(gamepad2.dpad_left){
            Robot.setCranePivotDegree(Robot.getCraneIMU().getYaw()-10);
        }
        //pivot cw
        else if(gamepad2.dpad_right){
            Robot.setCranePivotDegree(Robot.getCraneIMU().getYaw()+10);
        }
        //arm lift up
        if(gamepad2.dpad_up){
            Robot.setCraneLiftDegree(Robot.getCraneIMU().getRoll()+.1);
        }
        //arm lift down
        else if(gamepad2.dpad_down){
            Robot.setCraneLiftDegree(Robot.getCraneIMU().getRoll()-.1);
        }
        //bucket up
        if(gamepad2.right_bumper){
            Robot.getBucket().setDegree(Robot.getBucket().getDegree()+1);
        }
        //bucket down
        else if(gamepad2.left_bumper){
            Robot.getBucket().setDegree(Robot.getBucket().getDegree()-1);
        }
        if(gamepad1.dpad_up
                && Robot.getCraneIMU().getYaw()<= Robot.CRANE_COLLECTION_DROP.CRANE_PIVOT_DEGREE - Robot.ANGLE_RANGE
                && Robot.getCraneIMU().getYaw()>= Robot.CRANE_COLLECTION_DROP.CRANE_PIVOT_DEGREE + Robot.ANGLE_RANGE
                && Robot.getCraneIMU().getRoll()<=Robot.CRANE_COLLECTION_DROP.CRANE_LIFT_DEGREE - Robot.ANGLE_RANGE
                && Robot.getCraneIMU().getRoll()>=Robot.CRANE_COLLECTION_DROP.CRANE_LIFT_DEGREE + Robot.ANGLE_RANGE
                && Robot.getBucket().getDegree()<= Robot.CRANE_COLLECTION_DROP.BUCKET_DEGREE - Robot.ANGLE_RANGE
                && Robot.getBucket().getDegree()>= Robot.CRANE_COLLECTION_DROP.BUCKET_DEGREE + Robot.ANGLE_RANGE) {
                Robot.getIntake().runTime(-0.5, 3000);
                Robot.getBucket().setSlowDegree(Robot.CRANE_COLLECTION_HOLD.BUCKET_DEGREE,1000);
        }
        else if(gamepad1.dpad_down
                && Robot.getCraneIMU().getYaw() <= Robot.CRANE_COLLECTION_HOLD.CRANE_PIVOT_DEGREE - Robot.ANGLE_RANGE
                && Robot.getCraneIMU().getYaw() >= Robot.CRANE_COLLECTION_HOLD.CRANE_PIVOT_DEGREE + Robot.ANGLE_RANGE
                && Robot.getCraneIMU().getRoll() <= Robot.CRANE_COLLECTION_HOLD.CRANE_LIFT_DEGREE - Robot.ANGLE_RANGE
                && Robot.getCraneIMU().getRoll() >= Robot.CRANE_COLLECTION_HOLD.CRANE_LIFT_DEGREE + Robot.ANGLE_RANGE
                && Robot.getBucket().getDegree() <= Robot.CRANE_COLLECTION_HOLD.BUCKET_DEGREE - Robot.ANGLE_RANGE
                && Robot.getBucket().getDegree() >= Robot.CRANE_COLLECTION_HOLD.BUCKET_DEGREE + Robot.ANGLE_RANGE) {
                    //if bucket is outside go to intake
                    Robot.getIntake().runTime(0.5, 3000);
                    Robot.getBucket().setSlowDegree(Robot.CRANE_COLLECTION_DROP.BUCKET_DEGREE,1000);
        }
        //reset imu
        //help
        if(gamepad2.b){
           // Robot.getIMU().reset();
        }
        //drop
        //help with setSlowDegree
        if(gamepad2.a){
            //Robot.getBucket().setSlowDegree(/*insert degree*/,1.55);
        }
        if(gamepad2.x){
            Robot.getDrivetrain().stop();
        }
        //help
        //moving collection to preset or preset to preset
        //capping pickup
        if(gamepad2.left_stick_button){

        }
        //help
        //preset to preset
        //capping drop off
        if(gamepad2.right_stick_button){
            if(Robot.getCraneIMU().getPitch()>=-5
                    && Robot.getCraneIMU().getPitch()<=5){

            }
            else{

            }
        }
    }
}
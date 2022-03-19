Param(
    [double] $x = 100,
    [double] $y = 200,
    [double] $theta = 45
)

# FRC X coordinates just become Xbot Y coordinates
$xbotY = $x

# FRC Y coordinates a are a little more complicated. The field is 27 feet wide,
# so we need to take the height of the field in inches and subtract the FRC Y coordinate.

$xbotX = 27*12 - $y

# Finally, we add 90 degrees to the FRC theta to get the Xbot theta.

$xbotTheta = $theta + 90

Write-Host $xbotX, $xbotY, $xbotTheta

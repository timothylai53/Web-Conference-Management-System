# Frontend Setup Script
# Run this in PowerShell

Write-Host "🚀 ScholarSync Frontend Setup" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

# Check if we're in the frontend directory
if (-not (Test-Path "package.json")) {
    Write-Host "❌ Error: package.json not found!" -ForegroundColor Red
    Write-Host "Please run this script from the frontend directory" -ForegroundColor Yellow
    exit 1
}

Write-Host "📦 Installing dependencies..." -ForegroundColor Yellow
npm install

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Installation failed!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "✅ Installation complete!" -ForegroundColor Green
Write-Host ""
Write-Host "📋 Next Steps:" -ForegroundColor Cyan
Write-Host "1. Make sure your backend is running on http://localhost:8080" -ForegroundColor White
Write-Host "2. Run: npm start" -ForegroundColor White
Write-Host "3. Open: http://localhost:4200" -ForegroundColor White
Write-Host ""
Write-Host "📚 Documentation:" -ForegroundColor Cyan
Write-Host "- QUICK_START.md - Quick reference guide" -ForegroundColor White
Write-Host "- INTEGRATION_README.md - Full documentation" -ForegroundColor White
Write-Host "- INTEGRATION_SUMMARY.md - What was changed" -ForegroundColor White
Write-Host ""
Write-Host "🎉 Ready to run!" -ForegroundColor Green
